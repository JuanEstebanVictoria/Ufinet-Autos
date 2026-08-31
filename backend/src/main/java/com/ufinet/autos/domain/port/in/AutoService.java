package com.ufinet.autos.domain.port.in;

import com.ufinet.autos.infrastructure.web.dto.AutoRequestDTO;
import com.ufinet.autos.infrastructure.web.dto.AutoResponseDTO;
import com.ufinet.autos.domain.model.Auto;

import java.util.List;
import java.util.Optional;

/**
 * Service that handles business logic for car operations.
 */
public interface AutoService {

    List<AutoResponseDTO> searchAutos(Long userId, String plate, String brand, Integer year);

    Optional<AutoResponseDTO> getAutoByIdAndUserId(Long id, Long userId);

    AutoResponseDTO createAuto(AutoRequestDTO dto, Long userId);

    AutoResponseDTO updateAuto(Auto auto, AutoRequestDTO dto);

    void deleteAuto(Long id);

    Optional<Auto> getEntityByIdAndUserId(Long id, Long userId);
}
