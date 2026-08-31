package com.ufinet.autos.infrastructure.web.dto;

import com.ufinet.autos.domain.model.Auto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for outgoing auto responses.
 * Only exposes safe, client-relevant fields — never the user relationship or internal IDs.
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
     * Maps a domain {@link Auto} to its response DTO.
     *
     * @param auto the domain object to map
     * @return the populated DTO
     */
    public static AutoResponseDTO fromDomain(Auto auto) {
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
