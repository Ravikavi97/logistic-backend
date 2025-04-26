package com.logistics.service;

import com.logistics.dto.CreateAdminRequest;
import com.logistics.dto.UserDTO;
import com.logistics.entity.User;
import com.logistics.mapper.UserMapper;
import com.logistics.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Profile({"dev", "local", "test"}) // Only available in non-production environments
public class DevAdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final String devSecretKey;

    public DevAdminService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper,
            @Value("${app.dev-secret-key:your-dev-secret-key}") String devSecretKey) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.devSecretKey = devSecretKey;
    }

    @Transactional
    public UserDTO createAdminUser(CreateAdminRequest request) {
        // Validate secret key
        if (!devSecretKey.equals(request.getSecretKey())) {
            throw new IllegalArgumentException("Invalid secret key");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        // Create admin user
        User admin = new User();
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setFirstName(request.getFirstName());
        admin.setLastName(request.getLastName());
        admin.setRoles(Set.of("ADMIN"));
        admin.setPermissions(Set.of("ALL"));
        admin.setActive(true);

        User savedAdmin = userRepository.save(admin);
        return userMapper.toDTO(savedAdmin);
    }
} 