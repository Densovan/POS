package com.den.pos.service;

import com.den.pos.repository.auth.UserInfoProjection;
import com.den.pos.repository.auth.UserRepository;
import com.den.pos.web.dto.AuditUserRef;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserReferenceService {

    private final UserRepository userRepository;

    public UserReferenceService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Map<UUID, AuditUserRef> resolveReferences(UUID... userIds) {
        return doResolve(userIds == null ? List.of() : Arrays.asList(userIds));
    }

    @Transactional(readOnly = true)
    public Map<UUID, AuditUserRef> resolveReferences(Collection<UUID> userIds) {
        return doResolve(userIds);
    }

    private Map<UUID, AuditUserRef> doResolve(Collection<UUID> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Map.of();
        }
        Set<UUID> uniqueIds = userIds.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        if (uniqueIds.isEmpty()) {
            return Map.of();
        }

        Map<UUID, AuditUserRef> result = new HashMap<>();
        for (UserInfoProjection p : userRepository.findUserInfoByIds(uniqueIds)) {
            String name = p.getFirstName() != null || p.getLastName() != null
                    ? (nz(p.getFirstName()) + " " + nz(p.getLastName())).trim()
                    : null;
            result.put(p.getId(), new AuditUserRef(
                    p.getId(),
                    name,
                    p.getEmail(),
                    p.getEmployeeId(),
                    p.getIsActive(),
                    p.getPosition(),
                    p.getPhone()));
        }
        return result;
    }

    private static String nz(String value) {
        return value == null ? "" : value;
    }
}
