package com.ufinet.autos.infrastructure.persistence;

import com.ufinet.autos.domain.model.Auto;
import com.ufinet.autos.domain.port.out.AutoPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Infrastructure adapter that fulfills the {@link AutoPort} output port
 * using Spring Data JPA. This is the only class that knows about
 * {@link AutoEntity} and {@link AutoRepository} — the domain never imports them.
 */
@Component
@RequiredArgsConstructor
public class AutoRepositoryAdapter implements AutoPort {

    private final AutoRepository autoRepository;

    /**
     * JPA EntityManager used to create Hibernate proxies for FK associations.
     * Kept here (not in the service) so that JPA concerns stay in the infrastructure layer.
     */
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Auto> searchByUserId(Long userId, String plate, String brand, Integer year) {
        return autoRepository.searchByUserId(userId, plate, brand, year)
                .stream()
                .map(AutoEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Auto> findByIdAndUserId(Long id, Long userId) {
        return autoRepository.findByIdAndUserId(id, userId)
                .map(AutoEntity::toDomain);
    }

    /**
     * Persists a domain {@link Auto} to the database.
     * Creates a Hibernate proxy for the user FK — no extra SELECT issued.
     */
    @Override
    public Auto save(Auto auto) {
        UserEntity userRef = entityManager.getReference(UserEntity.class, auto.getUserId());
        AutoEntity entity = AutoEntity.fromDomain(auto, userRef);
        return autoRepository.save(entity).toDomain();
    }

    @Override
    public void deleteById(Long id) {
        autoRepository.deleteById(id);
    }
}
