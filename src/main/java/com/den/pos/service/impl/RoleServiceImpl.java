package com.den.pos.service.impl;

import com.den.pos.entity.auth.Permission;
import com.den.pos.entity.auth.Role;
import com.den.pos.entity.auth.RolePermission;
import com.den.pos.exception.BusinessException;
import com.den.pos.exception.ConflictException;
import com.den.pos.exception.ResourceNotFoundException;
import com.den.pos.repository.auth.PermissionRepository;
import com.den.pos.repository.auth.RolePermissionRepository;
import com.den.pos.repository.auth.RoleRepository;
import com.den.pos.service.RoleService;
import com.den.pos.web.dto.PermissionResponse;
import com.den.pos.web.dto.RolePermissionsRequest;
import com.den.pos.web.dto.RoleRequest;
import com.den.pos.web.dto.RoleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public RoleServiceImpl(RoleRepository roleRepository,
                           PermissionRepository permissionRepository,
                           RolePermissionRepository rolePermissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponse> findAll() {
        List<Role> roles = roleRepository.findAll();
        Map<UUID, Set<String>> keysByRole = loadPermissionKeys(
                roles.stream().map(Role::getId).toList());
        return roles.stream()
                .sorted(Comparator.comparing(Role::getName))
                .map(role -> toResponse(role, keysByRole.getOrDefault(role.getId(), Set.of())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponse getById(UUID id) {
        Role role = findRole(id);
        return toResponse(role, permissionKeysOf(role.getId()));
    }

    @Override
    @Transactional
    public RoleResponse create(RoleRequest request) {
        String name = request.name().trim();
        if (roleRepository.findByName(name).isPresent()) {
            throw new ConflictException("Role already exists: " + name);
        }
        Role role = new Role();
        role.setName(name);
        role.setDescription(request.description());
        role.setIsSystemRole(false);
        role.setCreatedAt(LocalDateTime.now());
        Role saved = roleRepository.saveAndFlush(role);
        return toResponse(saved, Set.of());
    }

    @Override
    @Transactional
    public RoleResponse update(UUID id, RoleRequest request) {
        Role role = findRole(id);
        requireCustomRole(role, "modified");
        String name = request.name().trim();
        if (!name.equals(role.getName()) && roleRepository.findByName(name).isPresent()) {
            throw new ConflictException("Role already exists: " + name);
        }
        role.setName(name);
        role.setDescription(request.description());
        Role saved = roleRepository.saveAndFlush(role);
        return toResponse(saved, permissionKeysOf(saved.getId()));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Role role = findRole(id);
        requireCustomRole(role, "deleted");
        roleRepository.delete(role);
        roleRepository.flush();
    }

    @Override
    @Transactional
    public RoleResponse setPermissions(UUID id, RolePermissionsRequest request) {
        Role role = findRole(id);

        Map<String, Permission> catalogue = permissionRepository.findAll().stream()
                .collect(Collectors.toMap(p -> key(p), Function.identity()));
        Set<String> requestedKeys = new LinkedHashSet<>(request.permissions());
        List<String> unknown = requestedKeys.stream()
                .filter(k -> !catalogue.containsKey(k))
                .toList();
        if (!unknown.isEmpty()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    "Unknown permissions: " + String.join(", ", unknown)
                            + ". See GET /roles/permissions for valid keys");
        }

        Set<UUID> desired = requestedKeys.stream()
                .map(catalogue::get)
                .map(Permission::getId)
                .collect(Collectors.toSet());
        Set<UUID> current = rolePermissionRepository.findByRoleId(id).stream()
                .map(RolePermission::getPermissionId)
                .collect(Collectors.toSet());

        for (UUID permissionId : desired) {
            if (!current.contains(permissionId)) {
                rolePermissionRepository.grant(id, permissionId);
            }
        }
        for (UUID permissionId : current) {
            if (!desired.contains(permissionId)) {
                rolePermissionRepository.revoke(id, permissionId);
            }
        }
        rolePermissionRepository.flush();

        Map<UUID, String> keyById = catalogue.values().stream()
                .collect(Collectors.toMap(Permission::getId, this::key));
        Set<String> keys = desired.stream()
                .map(keyById::get)
                .collect(Collectors.toCollection(TreeSet::new));
        return toResponse(role, keys);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermissionResponse> listPermissions() {
        return permissionRepository.findAll().stream()
                .sorted(Comparator.comparing(Permission::getModule).thenComparing(Permission::getAction))
                .map(p -> new PermissionResponse(p.getId(), p.getModule(), p.getAction(), key(p), p.getDescription()))
                .toList();
    }

    private Role findRole(UUID id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

    private void requireCustomRole(Role role, String action) {
        if (Boolean.TRUE.equals(role.getIsSystemRole())) {
            throw new ConflictException("System role '" + role.getName() + "' cannot be " + action
                    + ". Create a custom role instead");
        }
    }

    private Set<String> permissionKeysOf(UUID roleId) {
        List<UUID> permissionIds = rolePermissionRepository.findByRoleId(roleId).stream()
                .map(RolePermission::getPermissionId)
                .toList();
        return keysForIds(permissionIds);
    }

    private Map<UUID, Set<String>> loadPermissionKeys(List<UUID> roleIds) {
        List<RolePermission> junctions = rolePermissionRepository.findAll();
        Map<UUID, List<UUID>> permissionIdsByRole = junctions.stream()
                .collect(Collectors.groupingBy(RolePermission::getRoleId,
                        Collectors.mapping(RolePermission::getPermissionId, Collectors.toList())));
        Map<UUID, Set<String>> result = new TreeMap<>();
        for (UUID roleId : roleIds) {
            result.put(roleId, keysForIds(permissionIdsByRole.getOrDefault(roleId, List.of())));
        }
        return result;
    }

    private Set<String> keysForIds(List<UUID> permissionIds) {
        if (permissionIds.isEmpty()) {
            return Set.of();
        }
        Map<UUID, Permission> permissions = permissionRepository.findAllById(permissionIds).stream()
                .collect(Collectors.toMap(Permission::getId, Function.identity()));
        Set<String> keys = new TreeSet<>();
        for (UUID permissionId : new HashSet<>(permissionIds)) {
            Permission permission = permissions.get(permissionId);
            if (permission != null) {
                keys.add(key(permission));
            }
        }
        return keys;
    }

    private RoleResponse toResponse(Role role, Set<String> permissionKeys) {
        return new RoleResponse(role.getId(), role.getName(), role.getDescription(),
                Boolean.TRUE.equals(role.getIsSystemRole()), new ArrayList<>(permissionKeys), role.getCreatedAt());
    }

    private String key(Permission permission) {
        return permission.getModule() + ":" + permission.getAction();
    }
}
