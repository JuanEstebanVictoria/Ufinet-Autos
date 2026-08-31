package com.ufinet.autos.infrastructure.web;

import com.ufinet.autos.domain.exception.ResourceNotFoundException;
import com.ufinet.autos.domain.model.Auto;
import com.ufinet.autos.domain.port.in.AutoService;
import com.ufinet.autos.infrastructure.security.CustomUserDetails;
import com.ufinet.autos.infrastructure.web.dto.AutoRequestDTO;
import com.ufinet.autos.infrastructure.web.dto.AutoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller that handles all car-related endpoints.
 * Responsible for:
 * <ul>
 *   <li>Resolving the authenticated user's ID from the security context</li>
 *   <li>Mapping request DTOs to domain objects before calling the service</li>
 *   <li>Mapping domain objects to response DTOs before returning to the client</li>
 * </ul>
 * All routes are protected and scoped to the authenticated user's cars only.
 */
@RestController
@RequestMapping("/api/autos")
@RequiredArgsConstructor
public class AutoController {

    private final AutoService autoService;

    /**
     * Returns the authenticated user's database ID from the active security context.
     */
    private Long getCurrentUserId(Authentication auth) {
        return ((CustomUserDetails) auth.getPrincipal()).getId();
    }

    /**
     * Returns all cars for the authenticated user, with optional search filters.
     */
    @GetMapping
    public ResponseEntity<List<AutoResponseDTO>> getAllAutos(
            @RequestParam(required = false) String plate,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Integer year,
            Authentication auth) {
        List<AutoResponseDTO> result = autoService
                .searchAutos(getCurrentUserId(auth), plate, brand, year)
                .stream()
                .map(AutoResponseDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    /**
     * Returns a single car by ID. Returns 404 if it does not exist or belongs to another user.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AutoResponseDTO> getAutoById(@PathVariable Long id, Authentication auth) {
        Auto auto = autoService.getAutoByIdAndUserId(id, getCurrentUserId(auth))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Auto with id " + id + " not found or does not belong to the current user"));
        return ResponseEntity.ok(AutoResponseDTO.fromDomain(auto));
    }

    /**
     * Creates a new car and assigns it to the authenticated user.
     *
     * @param dto the car data from the request body
     */
    @PostMapping
    public ResponseEntity<AutoResponseDTO> createAuto(
            @Valid @RequestBody AutoRequestDTO dto, Authentication auth) {
        Auto auto = new Auto(null,
                dto.getBrand(), dto.getModel(), dto.getYear(),
                dto.getLicensePlate(), dto.getColor(),
                getCurrentUserId(auth));
        return ResponseEntity.ok(AutoResponseDTO.fromDomain(autoService.createAuto(auto)));
    }

    /**
     * Updates an existing car. Returns 404 if it does not exist or belongs to another user.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AutoResponseDTO> updateAuto(
            @PathVariable Long id,
            @Valid @RequestBody AutoRequestDTO dto,
            Authentication auth) {
        Auto existing = autoService.getAutoByIdAndUserId(id, getCurrentUserId(auth))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Auto with id " + id + " not found or does not belong to the current user"));

        existing.setBrand(dto.getBrand());
        existing.setModel(dto.getModel());
        existing.setYear(dto.getYear());
        existing.setLicensePlate(dto.getLicensePlate());
        existing.setColor(dto.getColor());

        return ResponseEntity.ok(AutoResponseDTO.fromDomain(autoService.updateAuto(existing)));
    }

    /**
     * Deletes a car by ID. Returns 404 if it does not exist or belongs to another user.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuto(@PathVariable Long id, Authentication auth) {
        autoService.getAutoByIdAndUserId(id, getCurrentUserId(auth))
                .ifPresentOrElse(
                        auto -> autoService.deleteAuto(id),
                        () -> { throw new ResourceNotFoundException(
                                "Auto with id " + id + " not found or does not belong to the current user"); });
        return ResponseEntity.noContent().build();
    }
}
