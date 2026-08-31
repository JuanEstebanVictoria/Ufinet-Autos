package com.ufinet.autos.domain.port.out;

import com.ufinet.autos.domain.model.Auto;

import java.util.List;
import java.util.Optional;

/**
 * Output port (secondary port) for car persistence operations.
 * Defines what the domain needs from storage — decoupled from any JPA details.
 */
public interface AutoPort {

    /**
     * Returns all cars for the given user, filtered by optional criteria.
     */
    List<Auto> searchByUserId(Long userId, String plate, String brand, Integer year);

    /**
     * Returns a single car by ID only if it belongs to the given user.
     */
    Optional<Auto> findByIdAndUserId(Long id, Long userId);

    /**
     * Persists a car entity and returns the saved instance.
     */
    Auto save(Auto auto);

    /**
     * Deletes a car by its primary key.
     */
    void deleteById(Long id);
}
