package com.ufinet.autos.domain.port.in;

import com.ufinet.autos.domain.model.Auto;

import java.util.List;
import java.util.Optional;

/**
 * Input port (primary port) for car-related use cases.
 * All methods work exclusively with domain objects — no DTOs, no JPA types.
 */
public interface AutoService {

    /**
     * Returns cars belonging to the given user, filtered by any combination of plate, brand, and year.
     */
    List<Auto> searchAutos(Long userId, String plate, String brand, Integer year);

    /**
     * Returns a single car by ID if it belongs to the given user.
     */
    Optional<Auto> getAutoByIdAndUserId(Long id, Long userId);

    /**
     * Persists a new car. The domain {@link Auto} must have a non-null {@code userId}.
     */
    Auto createAuto(Auto auto);

    /**
     * Persists an updated car.
     */
    Auto updateAuto(Auto auto);

    /**
     * Deletes a car by its primary key.
     */
    void deleteAuto(Long id);
}
