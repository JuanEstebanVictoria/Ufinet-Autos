package com.ufinet.autos.repository;

import com.ufinet.autos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/** Repository for user data. */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Returns a user by their username, or empty if not found.
     *
     * @param username the username to search for
     * @return an Optional containing the user, or empty if none exists
     */
    Optional<User> findByUsername(String username);
}
