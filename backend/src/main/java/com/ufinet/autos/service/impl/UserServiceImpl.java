package com.ufinet.autos.service.impl;

import com.ufinet.autos.exception.ResourceNotFoundException;
import com.ufinet.autos.model.User;
import com.ufinet.autos.repository.UserRepository;
import com.ufinet.autos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service for looking up users by username.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Returns a user by username, or throws 404 if not found.
     *
     * @param username the username to look up
     * @return the matching {@link User} entity
     * @throws ResourceNotFoundException if no user with that username exists
     */
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found: " + username));
    }
}
