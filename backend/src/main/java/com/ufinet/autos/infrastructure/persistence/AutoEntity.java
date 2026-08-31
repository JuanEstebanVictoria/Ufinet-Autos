package com.ufinet.autos.infrastructure.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ufinet.autos.domain.model.Auto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * JPA entity representing a row in the CARS table.
 * Mapped from/to domain {@link Auto} objects by the persistence adapter.
 * The domain layer never imports this class.
 */
@Entity(name = "Auto")
@Table(name = "CARS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AutoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(name = "[year]", nullable = false)
    private Integer year;

    @Column(name = "license_plate", nullable = false, unique = true)
    private String licensePlate;

    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private UserEntity user;

    /**
     * Maps this JPA entity to a pure domain {@link Auto} object.
     */
    public Auto toDomain() {
        return new Auto(id, brand, model, year, licensePlate, color,
                user != null ? user.getId() : null);
    }

    /**
     * Creates a JPA entity from a domain {@link Auto} and an existing {@link UserEntity} reference.
     *
     * @param auto    the domain object to map
     * @param userRef a Hibernate proxy or managed UserEntity for the FK
     */
    public static AutoEntity fromDomain(Auto auto, UserEntity userRef) {
        AutoEntity entity = new AutoEntity();
        if (auto.getId() != null) entity.setId(auto.getId());
        entity.setBrand(auto.getBrand());
        entity.setModel(auto.getModel());
        entity.setYear(auto.getYear());
        entity.setLicensePlate(auto.getLicensePlate());
        entity.setColor(auto.getColor());
        entity.setUser(userRef);
        return entity;
    }
}
