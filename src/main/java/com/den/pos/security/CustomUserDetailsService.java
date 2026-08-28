package com.den.pos.security;

import com.den.pos.entity.auth.User;
import com.den.pos.repository.auth.UserRepository;
import com.den.pos.repository.auth.UserRoleRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public CustomUserDetailsService(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        if (Boolean.FALSE.equals(user.getIsActive())) {
            throw new UsernameNotFoundException("User is disabled: " + email);
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userRoleRepository.findRoleNamesByUserId(user.getId())
                .forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())));
        userRoleRepository.findPermissionKeysByUserId(user.getId())
                .forEach(perm -> authorities.add(new SimpleGrantedAuthority(perm)));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPasswordHash())
                .authorities(authorities)
                .accountLocked(user.getLockedUntil() != null && user.getLockedUntil().isAfter(LocalDateTime.now()))
                .build();
    }
}
