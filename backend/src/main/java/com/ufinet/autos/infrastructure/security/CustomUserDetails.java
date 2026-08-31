package com.ufinet.autos.infrastructure.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Implementation of {@link UserDetails} that also carries the user's database ID.
 * This allows the security context to expose the user ID to controllers
 * without requiring an additional database query per request.
 */
@Getter
public class CustomUserDetails implements UserDetails {

    /** The user's primary key — used to scope database queries to this user's cars. */
    private final Long id;

    private final String username;

    /** BCrypt-encoded password — verified only during JWT token validation. */
    private final String password;

    public CustomUserDetails(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    /** No roles are used in this application — an empty list is correct. */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return true; }
}
