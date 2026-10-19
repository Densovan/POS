package com.den.pos.service;

import com.den.pos.web.dto.PermissionResponse;
import com.den.pos.web.dto.RolePermissionsRequest;
import com.den.pos.web.dto.RoleRequest;
import com.den.pos.web.dto.RoleResponse;

import java.util.List;
import java.util.UUID;

public interface RoleService {

    List<RoleResponse> findAll();

    RoleResponse getById(UUID id);

    RoleResponse create(RoleRequest request);

    RoleResponse update(UUID id, RoleRequest request);

    void delete(UUID id);

    /**
     * Replaces the role's permission set with the given module:action keys,
     * granting new ones and revoking missing ones. Effective on the next request.
     */
    RoleResponse setPermissions(UUID id, RolePermissionsRequest request);

    /** Full permission catalogue, sorted by module then action. */
    List<PermissionResponse> listPermissions();
}
