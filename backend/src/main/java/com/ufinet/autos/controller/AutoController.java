package com.ufinet.autos.controller;

import com.ufinet.autos.dto.AutoRequestDTO;
import com.ufinet.autos.dto.AutoResponseDTO;
import com.ufinet.autos.exception.ResourceNotFoundException;
import com.ufinet.autos.security.CustomUserDetails;
import com.ufinet.autos.service.AutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * REST controller that handles all car-related endpoints.
 * All routes are protected and scoped to the authenticated user's cars only.
 */
@RestController
@RequestMapping("/api/autos")
@RequiredArgsConstructor
public class AutoController {

    private final AutoService autoService;

    /**
     * Returns the authenticated user's database ID from the active security context.
     *
     * @param auth the current authentication token
     * @return the user's primary key
     */
    private Long getCurrentUserId(Authentication auth) {
        return ((CustomUserDetails) auth.getPrincipal()).getId();
    }

    /** Returns all cars belonging to the authenticated user. */
    @GetMapping
    public ResponseEntity<List<AutoResponseDTO>> getAllAutos(Authentication auth) {
        return ResponseEntity.ok(autoService.getAutosByUserId(getCurrentUserId(auth)));
    }

    /**
     * Returns a single car by ID. Returns 404 if it does not exist or belongs to another user.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AutoResponseDTO> getAutoById(@PathVariable Long id, Authentication auth) {
        AutoResponseDTO auto = autoService.getAutoByIdAndUserId(id, getCurrentUserId(auth))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Auto with id " + id + " not found or does not belong to the current user"));
        return ResponseEntity.ok(auto);
    }

    /**
     * Creates a new car and assigns it to the authenticated user.
     *
     * @param dto the car data
     * @return the created car
     */
    @PostMapping
    public ResponseEntity<AutoResponseDTO> createAuto(
            @Valid @RequestBody AutoRequestDTO dto, Authentication auth) {
        return ResponseEntity.ok(autoService.createAuto(dto, getCurrentUserId(auth)));
    }

    /**
     * Updates an existing car. Returns 404 if it does not exist or belongs to another user.
     *
     * @param id  the car ID to update
     * @param dto the new car data
     */
    @PutMapping("/{id}")
    public ResponseEntity<AutoResponseDTO> updateAuto(
            @PathVariable Long id,
            @Valid @RequestBody AutoRequestDTO dto,
            Authentication auth) {
        return autoService.getEntityByIdAndUserId(id, getCurrentUserId(auth))
                .map(auto -> ResponseEntity.ok(autoService.updateAuto(auto, dto)))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Auto with id " + id + " not found or does not belong to the current user"));
    }

    /**
     * Deletes a car by ID. Returns 404 if it does not exist or belongs to another user.
     *
     * @param id the car ID to delete
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuto(@PathVariable Long id, Authentication auth) {
        autoService.getEntityByIdAndUserId(id, getCurrentUserId(auth))
                .ifPresentOrElse(
                        auto -> autoService.deleteAuto(id),
                        () -> { throw new ResourceNotFoundException(
                                "Auto with id " + id + " not found or does not belong to the current user"); }
                );
        return ResponseEntity.noContent().build();
    }
}

