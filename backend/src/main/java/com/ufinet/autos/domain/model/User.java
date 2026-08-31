package com.ufinet.autos.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pure domain object representing a user of the application.
 * Contains no framework-specific annotations — no JPA, no Jackson.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String username;

    /**
     * BCrypt-encoded password.
     * Kept here so the domain can pass it to the security layer without
     * requiring a separate infrastructure call.
     */
    private String password;
}
