package com.ufinet.autos.dto;

import com.ufinet.autos.model.Auto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for outgoing auto responses.
 * Only exposes safe, client-relevant fields — never the User relationship.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutoResponseDTO {

    private Long id;
    private String brand;
    private String model;
    private Integer year;
    private String licensePlate;
    private String color;

    /**
     * Maps an Auto entity to its response DTO.
     *
     * @param auto the entity to map
     * @return the populated DTO
     */
    public static AutoResponseDTO fromEntity(Auto auto) {
        return new AutoResponseDTO(
                auto.getId(),
                auto.getBrand(),
                auto.getModel(),
                auto.getYear(),
                auto.getLicensePlate(),
                auto.getColor()
        );
    }
}
