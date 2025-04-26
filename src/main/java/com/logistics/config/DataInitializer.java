package com.logistics.config;

import com.logistics.entity.User;
import com.logistics.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create admin user if not exists
            if (!userRepository.existsByEmail("admin@logistics.com")) {
                User admin = new User();
                admin.setEmail("admin@logistics.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setFirstName("Admin");
                admin.setLastName("User");
                admin.setRoles(Set.of("ADMIN"));
                admin.setPermissions(Set.of("ALL"));
                admin.setActive(true);
                userRepository.save(admin);
            }

            // Create regular user if not exists
            if (!userRepository.existsByEmail("user@logistics.com")) {
                User user = new User();
                user.setEmail("user@logistics.com");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setFirstName("Regular");
                user.setLastName("User");
                user.setRoles(Set.of("USER"));
                user.setPermissions(Set.of("READ"));
                user.setActive(true);
                userRepository.save(user);
            }
        };
    }
} 