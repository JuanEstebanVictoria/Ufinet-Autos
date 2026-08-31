package com.ufinet.autos.infrastructure.persistence;

import com.ufinet.autos.domain.model.User;
import com.ufinet.autos.domain.port.out.UserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Infrastructure adapter that fulfills the {@link UserPort} output port
 * using Spring Data JPA. The domain never imports this class directly.
 */
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserPort {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
