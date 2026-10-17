package com.den.pos.repository.auth;

import java.util.UUID;

public interface UserInfoProjection {

    UUID getId();

    String getEmail();

    UUID getEmployeeId();

    Boolean getIsActive();

    String getFirstName();

    String getLastName();

    String getPosition();

    String getPhone();
}
