package com.logistics.dto;

import com.logistics.entity.User;
import lombok.Data;

@Data
public class LoginResponse {
    private UserDTO user;
    private String token;

    @Data
    public static class UserDTO {
        private String id;
        private String email;
        private String firstName;
        private String lastName;
        private boolean isActive;

        public static UserDTO fromUser(User user) {
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setEmail(user.getEmail());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setActive(user.isActive());
            return dto;
        }
    }
} 