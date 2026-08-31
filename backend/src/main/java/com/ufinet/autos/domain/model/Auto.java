package com.ufinet.autos.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pure domain object representing an automobile.
 * Contains no framework-specific annotations — no JPA, no Jackson.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auto {

    private Long id;
    private String brand;
    private String model;
    private Integer year;
    private String licensePlate;
    private String color;

    /**
     * The owner's primary key — stored as a plain Long (not a JPA relation).
     * The persistence adapter is responsible for resolving this to a FK reference.
     */
    private Long userId;
}
