package com.logistics.controller;

import com.logistics.dto.ApiResponse;
import com.logistics.dto.LoginRequest;
import com.logistics.dto.LoginResponse;
import com.logistics.entity.User;
import com.logistics.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletRequest request) {
        log.debug("REST request to login user : {}", loginRequest.getEmail());
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.success(response, "Login successful")
            .withPath(request.getRequestURI())
            .withRequestId(UUID.randomUUID().toString()));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> getCurrentUser(HttpServletRequest request) {
        log.debug("REST request to get current user");
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.success(user, "Current user retrieved successfully")
            .withPath(request.getRequestURI())
            .withRequestId(UUID.randomUUID().toString()));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {
        log.debug("REST request to logout user");
        // JWT tokens are stateless, so we don't need to do anything server-side
        return ResponseEntity.ok(ApiResponse.<Void>success(null, "Logout successful")
            .withPath(request.getRequestURI())
            .withRequestId(UUID.randomUUID().toString()));
    }
} 