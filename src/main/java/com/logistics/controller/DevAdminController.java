package com.logistics.controller;

import com.logistics.dto.ApiResponse;
import com.logistics.dto.CreateAdminRequest;
import com.logistics.dto.UserDTO;
import com.logistics.service.DevAdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/dev")
@Profile({"dev", "local", "test"}) // Only available in non-production environments
public class DevAdminController {

    private final DevAdminService devAdminService;

    public DevAdminController(DevAdminService devAdminService) {
        this.devAdminService = devAdminService;
    }

    @PostMapping("/admin")
    public ResponseEntity<ApiResponse<UserDTO>> createAdminUser(
            @Valid @RequestBody CreateAdminRequest request,
            HttpServletRequest httpRequest) {
        log.debug("REST request to create Admin User : {}", request.getEmail());
        UserDTO created = devAdminService.createAdminUser(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(created, "Admin user created successfully")
                .withPath(httpRequest.getRequestURI())
                .withRequestId(UUID.randomUUID().toString()));
    }
} 