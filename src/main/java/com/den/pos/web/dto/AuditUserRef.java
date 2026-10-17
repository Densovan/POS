package com.den.pos.web.dto;

import java.util.UUID;

public record AuditUserRef(
        UUID id,
        String name,
        String email,
        UUID employeeId,
        Boolean isActive,
        String position,
        String phone
) {
}
