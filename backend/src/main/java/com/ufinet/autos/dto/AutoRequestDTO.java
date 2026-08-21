package com.ufinet.autos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Request body for creating or updating a car.
 * All fields are validated before reaching the service layer.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutoRequestDTO {

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Model is required")
    private String model;

    @NotNull(message = "Year is required")
    @Min(value = 1900, message = "Year must be 1900 or later")
    @Max(value = 2100, message = "Year is not realistic")
    private Integer year;

    @NotBlank(message = "License plate is required")
    @Pattern(regexp = "^[A-Z0-9\\-]+$", message = "License plate must contain only uppercase letters, numbers, and hyphens")
    private String licensePlate;

    private String color;
}

