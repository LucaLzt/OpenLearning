package com.projects.openlearning.identity.internal.security;

import com.projects.openlearning.common.security.api.AuthenticatedUser;
import com.projects.openlearning.identity.internal.model.Role;
import com.projects.openlearning.identity.internal.model.User;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public record SecurityUser(User user) implements UserDetails, AuthenticatedUser {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user.getRole() == null) {
            return Collections.emptyList();
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public UUID getUserId() {
        return user.getId();
    }

    @Override
    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public String getRole() {
        return user.getRole().name();
    }
}
