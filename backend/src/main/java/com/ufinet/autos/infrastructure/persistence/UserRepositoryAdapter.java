package com.ufinet.autos.infrastructure.persistence;

import com.ufinet.autos.domain.model.User;
import com.ufinet.autos.domain.port.out.UserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Infrastructure adapter that fulfills the {@link UserPort} output port
 * using Spring Data JPA. This is the only class that knows about
 * {@link UserEntity} and {@link UserRepository} — the domain never imports them.
 */
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserPort {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserEntity::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserEntity.fromDomain(user);
        return userRepository.save(entity).toDomain();
    }
}
