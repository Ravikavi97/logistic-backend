package com.logistics.service;

import com.logistics.dto.CreateUserRequest;
import com.logistics.dto.UserDTO;
import com.logistics.entity.User;
import com.logistics.exception.ResourceNotFoundException;
import com.logistics.mapper.UserMapper;
import com.logistics.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthService authService;

    public UserService(UserRepository userRepository, UserMapper userMapper, AuthService authService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.authService = authService;
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(String id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Transactional
    public UserDTO createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        // Only admins can create users with admin role
        User currentUser = authService.getCurrentUser();
        if (request.getRoles() != null && 
            request.getRoles().contains("ADMIN") && 
            !currentUser.getRoles().contains("ADMIN")) {
            throw new AccessDeniedException("Only admins can create admin users");
        }

        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    @Transactional
    public UserDTO updateUser(String id, CreateUserRequest request) {
        User currentUser = authService.getCurrentUser();
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Only admins can update other users
        if (!currentUser.getId().equals(id) && !currentUser.getRoles().contains("ADMIN")) {
            throw new AccessDeniedException("You can only update your own profile");
        }

        // Only admins can modify roles and permissions
        if (!currentUser.getRoles().contains("ADMIN")) {
            request.setRoles(userToUpdate.getRoles());
            request.setPermissions(userToUpdate.getPermissions());
        }

        // Prevent removing admin role from the last admin
        if (userToUpdate.getRoles().contains("ADMIN") && 
            (request.getRoles() == null || !request.getRoles().contains("ADMIN"))) {
            long adminCount = userRepository.countByRolesContaining("ADMIN");
            if (adminCount == 1) {
                throw new IllegalStateException("Cannot remove admin role from the last admin user");
            }
        }

        userMapper.updateEntity(userToUpdate, request);
        User updatedUser = userRepository.save(userToUpdate);
        return userMapper.toDTO(updatedUser);
    }

    @Transactional
    public void deleteUser(String id) {
        User currentUser = authService.getCurrentUser();
        
        // Prevent self-deletion
        if (currentUser.getId().equals(id)) {
            throw new IllegalStateException("Cannot delete your own account");
        }

        // Only admins can delete users
        if (!currentUser.getRoles().contains("ADMIN")) {
            throw new AccessDeniedException("Only admins can delete users");
        }

        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Prevent deleting the last admin
        if (userToDelete.getRoles().contains("ADMIN")) {
            long adminCount = userRepository.countByRolesContaining("ADMIN");
            if (adminCount == 1) {
                throw new IllegalStateException("Cannot delete the last admin user");
            }
        }

        userRepository.deleteById(id);
    }
} 