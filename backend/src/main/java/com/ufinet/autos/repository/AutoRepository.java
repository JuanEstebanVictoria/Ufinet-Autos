package com.ufinet.autos.repository;

import com.ufinet.autos.model.Auto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for car data. All queries are scoped by the owner's user ID.
 */
@Repository
public interface AutoRepository extends JpaRepository<Auto, Long> {

    /**
     * Returns all cars belonging to the given user, with optional filters.
     * Each filter parameter is optional — passing {@code null} skips that condition.
     * Plate and brand matching is case-insensitive and partial (contains).
     *
     * @param userId the owner's primary key
     * @param plate  partial license plate to match, or {@code null} to skip
     * @param brand  partial brand name to match, or {@code null} to skip
     * @param year   exact year to match, or {@code null} to skip
     * @return filtered list of cars owned by that user
     */
    @Query("SELECT a FROM Auto a WHERE a.user.id = :userId " +
           "AND (:plate IS NULL OR LOWER(a.licensePlate) LIKE LOWER(CONCAT('%', :plate, '%'))) " +
           "AND (:brand IS NULL OR LOWER(a.brand) LIKE LOWER(CONCAT('%', :brand, '%'))) " +
           "AND (:year IS NULL OR a.year = :year)")
    List<Auto> searchByUserId(
            @Param("userId") Long userId,
            @Param("plate") String plate,
            @Param("brand") String brand,
            @Param("year") Integer year);

    /**
     * Returns a single car by ID, only if it belongs to the given user.
     * Returns empty if the car does not exist or belongs to a different user.
     *
     * @param id     the car's primary key
     * @param userId the owner's primary key
     * @return the car if found and owned by that user, or empty
     */
    @Query("SELECT a FROM Auto a WHERE a.id = :id AND a.user.id = :userId")
    Optional<Auto> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
}
