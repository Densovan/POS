package com.den.pos.service.impl;

import com.den.pos.entity.auth.Role;
import com.den.pos.entity.auth.User;
import com.den.pos.exception.BusinessException;
import com.den.pos.exception.ResourceNotFoundException;
import com.den.pos.repository.auth.RoleRepository;
import com.den.pos.repository.auth.UserRepository;
import com.den.pos.repository.auth.UserRoleNameProjection;
import com.den.pos.repository.auth.UserRoleRepository;
import com.den.pos.service.UserService;
import com.den.pos.web.dto.RoleSummary;
import com.den.pos.web.dto.UserResponse;
import com.den.pos.web.dto.UserRolesRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        List<User> users = userRepository.findAll();
        Map<UUID, List<RoleSummary>> rolesByUser = loadRoles(
                users.stream().map(User::getId).toList());
        return users.stream()
                .sorted(Comparator.comparing(User::getEmail))
                .map(user -> toResponse(user, rolesByUser.getOrDefault(user.getId(), List.of())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(UUID id) {
        User user = findUser(id);
        return toResponse(user, loadRoles(List.of(id)).getOrDefault(id, List.of()));
    }

    @Override
    @Transactional
    public UserResponse setRoles(UUID targetUserId, UUID actingUserId, UserRolesRequest request) {
        if (actingUserId != null && actingUserId.equals(targetUserId)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    "You cannot change your own roles. Ask another admin to do it");
        }
        User user = findUser(targetUserId);

        List<Role> roles = roleRepository.findAllById(request.roleIds());
        if (roles.size() != new HashSet<>(request.roleIds()).size()) {
            throw new ResourceNotFoundException("One or more roles not found. Copy valid ids from GET /roles");
        }

        userRoleRepository.revokeAllForUser(targetUserId);
        roles.forEach(role -> userRoleRepository.grant(targetUserId, role.getId()));
        userRoleRepository.flush();

        List<RoleSummary> summaries = roles.stream()
                .sorted(Comparator.comparing(Role::getName))
                .map(role -> new RoleSummary(role.getId(), role.getName()))
                .toList();
        return toResponse(user, summaries);
    }

    private User findUser(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Map<UUID, List<RoleSummary>> loadRoles(Collection<UUID> userIds) {
        if (userIds.isEmpty()) {
            return Map.of();
        }
        Map<String, Role> rolesByName = roleRepository.findAll().stream()
                .collect(Collectors.toMap(Role::getName, Function.identity()));
        return userRoleRepository.findRoleNamesByUserIds(userIds).stream()
                .collect(Collectors.groupingBy(UserRoleNameProjection::getUserId,
                        Collectors.mapping(projection -> toSummary(rolesByName, projection.getRoleName()),
                                Collectors.toList())));
    }

    private RoleSummary toSummary(Map<String, Role> rolesByName, String roleName) {
        Role role = rolesByName.get(roleName);
        return role != null ? new RoleSummary(role.getId(), role.getName()) : new RoleSummary(null, roleName);
    }

    private UserResponse toResponse(User user, List<RoleSummary> roles) {
        return new UserResponse(user.getId(), user.getEmail(), user.getIsActive(),
                user.getLastLoginAt(), new ArrayList<>(roles));
    }
}
