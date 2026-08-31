package com.ufinet.autos.infrastructure.security;

import com.ufinet.autos.infrastructure.persistence.UserEntity;
import com.ufinet.autos.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Loads user data from the database for Spring Security authentication.
 * Uses {@link UserEntity} directly — this is infrastructure talking to infrastructure,
 * which is correct. No domain model is involved.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Finds the user entity by username and returns their details for authentication.
     *
     * @param username the username to look up
     * @return a {@link CustomUserDetails} object with the user's id, username, and password
     * @throws UsernameNotFoundException if no user with that username exists
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username: " + username));

        return new CustomUserDetails(user.getId(), user.getUsername(), user.getPassword());
    }
}
