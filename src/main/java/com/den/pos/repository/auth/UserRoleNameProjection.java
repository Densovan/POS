package com.den.pos.repository.auth;

import java.util.UUID;

/**
 * Row projection of user_roles ⋈ roles: which role name belongs to which user.
 */
public interface UserRoleNameProjection {

    UUID getUserId();

    String getRoleName();
}
