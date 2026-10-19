package com.den.pos.controller;

import com.den.pos.common.UserIdAware;
import com.den.pos.service.UserService;
import com.den.pos.web.dto.ApiResponse;
import com.den.pos.web.dto.UserResponse;
import com.den.pos.web.dto.UserRolesRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "Users", description = "User accounts and role assignment")
@RestController
@RequestMapping("/users")
@PreAuthorize("hasAuthority('users:read')")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "List all users", description = "Each user with their assigned roles; never includes credentials")
    @GetMapping
    public ApiResponse<List<UserResponse>> findAll() {
        return ApiResponse.success(userService.findAll(), "Users retrieved successfully");
    }

    @Operation(summary = "Get a user by id")
    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getById(
            @Parameter(description = "User UUID") @PathVariable UUID id) {
        return ApiResponse.success(userService.getById(id), "User retrieved successfully");
    }

    @Operation(summary = "Set a user's roles",
            description = "Requires users:manage. Full replacement set: listed roles are assigned, missing ones revoked. Takes effect on the next request. You cannot change your own roles")
    @PutMapping(value = "/{id}/roles", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('users:manage')")
    public ApiResponse<UserResponse> setRoles(
            @Parameter(description = "User UUID") @PathVariable UUID id,
            @Valid @RequestBody UserRolesRequest request,
            Authentication authentication) {
        UUID actingUserId = authentication != null
                && authentication.getPrincipal() instanceof UserIdAware aware ? aware.getId() : null;
        return ApiResponse.success(userService.setRoles(id, actingUserId, request),
                "User roles updated successfully");
    }
}
