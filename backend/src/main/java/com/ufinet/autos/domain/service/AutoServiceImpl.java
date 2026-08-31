package com.ufinet.autos.domain.service;

import com.ufinet.autos.infrastructure.web.dto.AutoRequestDTO;
import com.ufinet.autos.infrastructure.web.dto.AutoResponseDTO;
import com.ufinet.autos.domain.model.Auto;
import com.ufinet.autos.domain.model.User;
import com.ufinet.autos.domain.port.out.AutoPort;
import com.ufinet.autos.domain.port.in.AutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service that handles business logic for car operations.
 * Converts between Auto entities and DTOs for all CRUD operations.
 */
@Service
@RequiredArgsConstructor
public class AutoServiceImpl implements AutoService {

    private final AutoPort autoPort;

    /**
     * JPA EntityManager used to create Hibernate proxies for FK associations.
     * Injected with @PersistenceContext as required by the JPA spec.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Returns cars belonging to the given user, filtered by any combination of plate, brand, and year.
     * Pass {@code null} for any filter to ignore it — passing all nulls returns every car for the user.
     *
     * @param userId the owner's primary key
     * @param plate  partial license plate (case-insensitive), or {@code null}
     * @param brand  partial brand name (case-insensitive), or {@code null}
     * @param year   exact year to match, or {@code null}
     * @return filtered list of car response DTOs
     */
    @Transactional(readOnly = true)
    public List<AutoResponseDTO> searchAutos(Long userId, String plate, String brand, Integer year) {
        return autoPort.searchByUserId(userId, plate, brand, year)
                .stream()
                .map(AutoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Returns a single car by ID if it belongs to the given user.
     * Returns empty if not found or if the car belongs to a different user.
     *
     * @param id     the car's primary key
     * @param userId the owner's primary key
     * @return optional car DTO
     */
    @Transactional(readOnly = true)
    public Optional<AutoResponseDTO> getAutoByIdAndUserId(Long id, Long userId) {
        return autoPort.findByIdAndUserId(id, userId)
                .map(AutoResponseDTO::fromEntity);
    }

    /**
     * Creates and persists a new car assigned to the given user.
     *
     * @param dto    the car data from the request
     * @param userId the owner's primary key
     * @return the saved car as a response DTO
     */
    @Transactional
    public AutoResponseDTO createAuto(AutoRequestDTO dto, Long userId) {
        Auto auto = new Auto();
        auto.setBrand(dto.getBrand());
        auto.setModel(dto.getModel());
        auto.setYear(dto.getYear());
        auto.setLicensePlate(dto.getLicensePlate());
        auto.setColor(dto.getColor());

        // getReference() creates a Hibernate proxy — no SELECT is issued for the User.
        // Hibernate uses the id directly as the FK value when writing the INSERT.
        auto.setUser(entityManager.getReference(User.class, userId));

        return AutoResponseDTO.fromEntity(autoPort.save(auto));
    }

    /**
     * Updates an existing car's fields with data from the request DTO.
     *
     * @param auto the car entity to update (already fetched and ownership-verified)
     * @param dto  the new field values
     * @return the updated car as a response DTO
     */
    @Transactional
    public AutoResponseDTO updateAuto(Auto auto, AutoRequestDTO dto) {
        auto.setBrand(dto.getBrand());
        auto.setModel(dto.getModel());
        auto.setYear(dto.getYear());
        auto.setLicensePlate(dto.getLicensePlate());
        auto.setColor(dto.getColor());
        return AutoResponseDTO.fromEntity(autoPort.save(auto));
    }

    /**
     * Deletes a car by its primary key.
     *
     * @param id the car's primary key
     */
    @Transactional
    public void deleteAuto(Long id) {
        autoPort.deleteById(id);
    }

    /**
     * Returns the raw Auto entity if it belongs to the given user.
     * Used internally by update and delete operations before modifying data.
     *
     * @param id     the car's primary key
     * @param userId the owner's primary key
     * @return the managed Auto entity, or empty if not found / not owned
     */
    @Transactional(readOnly = true)
    public Optional<Auto> getEntityByIdAndUserId(Long id, Long userId) {
        return autoPort.findByIdAndUserId(id, userId);
    }
}

