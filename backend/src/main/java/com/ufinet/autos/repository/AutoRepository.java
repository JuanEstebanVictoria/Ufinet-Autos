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
     * Returns all cars belonging to the given user.
     *
     * @param userId the owner's primary key
     * @return list of cars owned by that user
     */
    @Query("SELECT a FROM Auto a WHERE a.user.id = :userId")
    List<Auto> findByUserId(@Param("userId") Long userId);

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
