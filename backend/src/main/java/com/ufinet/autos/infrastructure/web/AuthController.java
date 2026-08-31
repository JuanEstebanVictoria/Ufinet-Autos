package com.ufinet.autos.infrastructure.web;

import com.ufinet.autos.infrastructure.web.dto.AuthRequestDTO;
import com.ufinet.autos.infrastructure.web.dto.AuthResponseDTO;
import com.ufinet.autos.domain.model.User;
import com.ufinet.autos.domain.port.out.UserPort;
import com.ufinet.autos.infrastructure.security.CustomUserDetails;
import com.ufinet.autos.infrastructure.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * REST controller for user login and registration.
 * Returns a JWT token on successful login.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private final UserPort userPort;
    private final PasswordEncoder passwordEncoder;

    /**
     * Authenticates the user with username and password and returns a signed JWT token.
     *
     * @param dto username and password
     * @return JWT token on success, or 401 if the credentials are invalid
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequestDTO dto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(java.util.Map.of("message", "Invalid username or password"));
        }
        final CustomUserDetails userDetails =
                (CustomUserDetails) userDetailsService.loadUserByUsername(dto.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

    /**
     * Registers a new user account with an encoded password.
     *
     * @param dto username and password for the new user
     * @return success message, or 400 if the username is already taken
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthRequestDTO dto) {
        if (userPort.existsByUsername(dto.getUsername())) {
            return ResponseEntity.badRequest().body(java.util.Map.of("message", "Username already exists"));
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userPort.save(user);
        return ResponseEntity.ok(java.util.Map.of("message", "User registered successfully"));
    }
}

