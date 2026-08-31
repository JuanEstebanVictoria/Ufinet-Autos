package com.ufinet.autos.domain.port.out;

import com.ufinet.autos.domain.model.User;

import java.util.Optional;

/**
 * Output port (secondary port) for user persistence operations.
 * Defines what the domain needs from storage — decoupled from any JPA details.
 */
public interface UserPort {

    /**
     * Returns a user by username, or empty if not found.
     */
    Optional<User> findByUsername(String username);

    /**
     * Returns true if a user with the given username already exists.
     */
    boolean existsByUsername(String username);

    /**
     * Persists a user entity and returns the saved instance.
     */
    User save(User user);
}
