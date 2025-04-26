package com.logistics.mapper;

import com.logistics.dto.CreateUserRequest;
import com.logistics.dto.UserDTO;
import com.logistics.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class UserMapper {
    
    private final PasswordEncoder passwordEncoder;
    
    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    
    public UserDTO toDTO(User entity) {
        if (entity == null) {
            return null;
        }
        
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setRoles(entity.getRoles());
        dto.setPermissions(entity.getPermissions());
        dto.setActive(entity.isActive());
        dto.setLastLogin(entity.getLastLogin());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
    
    public User toEntity(CreateUserRequest request) {
        if (request == null) {
            return null;
        }
        
        User entity = new User();
        entity.setEmail(request.getEmail());
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setRoles(request.getRoles() != null ? request.getRoles() : new HashSet<>());
        entity.setPermissions(request.getPermissions() != null ? request.getPermissions() : new HashSet<>());
        entity.setActive(request.isActive());
        return entity;
    }
    
    public void updateEntity(User entity, CreateUserRequest request) {
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            entity.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        if (request.getRoles() != null) {
            entity.setRoles(request.getRoles());
        }
        if (request.getPermissions() != null) {
            entity.setPermissions(request.getPermissions());
        }
        entity.setActive(request.isActive());
    }
} 