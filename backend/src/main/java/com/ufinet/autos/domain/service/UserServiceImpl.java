package com.ufinet.autos.domain.service;

import com.ufinet.autos.domain.exception.ResourceNotFoundException;
import com.ufinet.autos.domain.model.User;
import com.ufinet.autos.domain.port.out.UserPort;
import com.ufinet.autos.domain.port.in.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service for looking up users by username.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserPort userPort;

    /**
     * Returns a user by username, or throws 404 if not found.
     *
     * @param username the username to look up
     * @return the matching {@link User} entity
     * @throws ResourceNotFoundException if no user with that username exists
     */
    public User getByUsername(String username) {
        return userPort.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found: " + username));
    }
}
