package com.ufinet.autos.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link UserEntity}.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Returns a user entity by their username, or empty if not found.
     *
     * @param username the username to search for
     * @return an Optional containing the entity, or empty if none exists
     */
    Optional<UserEntity> findByUsername(String username);
}
