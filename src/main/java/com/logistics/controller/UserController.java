package com.logistics.controller;

import com.logistics.dto.ApiResponse;
import com.logistics.dto.CreateUserRequest;
import com.logistics.dto.PageResponse;
import com.logistics.dto.UserDTO;
import com.logistics.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<UserDTO>>> getAllUsers(
            @PageableDefault(size = 20, sort = "email") Pageable pageable,
            HttpServletRequest request) {
        log.debug("REST request to get all Users with pagination: {}", pageable);
        Page<UserDTO> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(ApiResponse.successPage(users)
            .withPath(request.getRequestURI())
            .withRequestId(UUID.randomUUID().toString()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @authService.getCurrentUser().getId() == #id")
    public ResponseEntity<ApiResponse<UserDTO>> getUser(
            @PathVariable String id,
            HttpServletRequest request) {
        log.debug("REST request to get User : {}", id);
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user, "User retrieved successfully")
            .withPath(request.getRequestURI())
            .withRequestId(UUID.randomUUID().toString()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDTO>> createUser(
            @Valid @RequestBody CreateUserRequest request,
            HttpServletRequest httpRequest) {
        log.debug("REST request to create User : {}", request.getEmail());
        UserDTO created = userService.createUser(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(created, "User created successfully")
                .withPath(httpRequest.getRequestURI())
                .withRequestId(UUID.randomUUID().toString()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @authService.getCurrentUser().getId() == #id")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @PathVariable String id,
            @Valid @RequestBody CreateUserRequest request,
            HttpServletRequest httpRequest) {
        log.debug("REST request to update User : {}", id);
        UserDTO updated = userService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "User updated successfully")
            .withPath(httpRequest.getRequestURI())
            .withRequestId(UUID.randomUUID().toString()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable String id,
            HttpServletRequest request) {
        log.debug("REST request to delete User : {}", id);
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.<Void>success(null, "User deleted successfully")
            .withPath(request.getRequestURI())
            .withRequestId(UUID.randomUUID().toString()));
    }
} 