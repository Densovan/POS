package com.den.pos.controller;

import com.den.pos.service.RoleService;
import com.den.pos.web.dto.ApiResponse;
import com.den.pos.web.dto.PermissionResponse;
import com.den.pos.web.dto.RolePermissionsRequest;
import com.den.pos.web.dto.RoleRequest;
import com.den.pos.web.dto.RoleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "Roles", description = "Role and permission management. Changes apply to the next request — no restart or re-login needed")
@RestController
@RequestMapping("/roles")
@PreAuthorize("hasAuthority('roles:read')")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Operation(summary = "List all roles", description = "Includes each role's granted permission keys")
    @GetMapping
    public ApiResponse<List<RoleResponse>> findAll() {
        return ApiResponse.success(roleService.findAll(), "Roles retrieved successfully");
    }

    @Operation(summary = "List the permission catalogue", description = "Every module:action key that can be granted to a role")
    @GetMapping("/permissions")
    public ApiResponse<List<PermissionResponse>> listPermissions() {
        return ApiResponse.success(roleService.listPermissions(), "Permissions retrieved successfully");
    }

    @Operation(summary = "Get a role by id")
    @GetMapping("/{id}")
    public ApiResponse<RoleResponse> getById(
            @Parameter(description = "Role UUID") @PathVariable UUID id) {
        return ApiResponse.success(roleService.getById(id), "Role retrieved successfully");
    }

    @Operation(summary = "Create a custom role", description = "Requires roles:manage. New roles start with no permissions")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('roles:manage')")
    public ResponseEntity<ApiResponse<RoleResponse>> create(@Valid @RequestBody RoleRequest request) {
        RoleResponse response = roleService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location)
                .body(ApiResponse.success(response, "Role created successfully"));
    }

    @Operation(summary = "Rename / describe a role", description = "Requires roles:manage. System roles (owner, admin, manager, cashier) cannot be modified")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('roles:manage')")
    public ApiResponse<RoleResponse> update(
            @Parameter(description = "Role UUID") @PathVariable UUID id,
            @Valid @RequestBody RoleRequest request) {
        return ApiResponse.success(roleService.update(id, request), "Role updated successfully");
    }

    @Operation(summary = "Set a role's permissions",
            description = "Requires roles:manage. Full replacement set: listed keys are granted, missing keys are revoked. Takes effect on the next request")
    @PutMapping(value = "/{id}/permissions", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('roles:manage')")
    public ApiResponse<RoleResponse> setPermissions(
            @Parameter(description = "Role UUID") @PathVariable UUID id,
            @Valid @RequestBody RolePermissionsRequest request) {
        return ApiResponse.success(roleService.setPermissions(id, request), "Role permissions updated successfully");
    }

    @Operation(summary = "Delete a custom role", description = "Requires roles:manage. System roles cannot be deleted; users keep their other roles")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('roles:manage')")
    public ApiResponse<Void> delete(
            @Parameter(description = "Role UUID") @PathVariable UUID id) {
        roleService.delete(id);
        return ApiResponse.successMessage("Role deleted successfully");
    }
}
