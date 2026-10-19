package com.den.pos.service;

import com.den.pos.web.dto.UserResponse;
import com.den.pos.web.dto.UserRolesRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<UserResponse> findAll();

    UserResponse getById(UUID id);

    /**
     * Replaces the user's roles with the given role ids. Effective on the next request.
     * The acting user cannot change their own roles (accidental lock-out guard).
     */
    UserResponse setRoles(UUID targetUserId, UUID actingUserId, UserRolesRequest request);
}
