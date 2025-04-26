package com.logistics.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserDTO {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private Set<String> roles;
    private Set<String> permissions;
    private boolean isActive;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @JsonIgnore
    private String password; // Exclude password from JSON responses
} 