package com.ufinet.autos.infrastructure.persistence;

import com.ufinet.autos.domain.model.Auto;
import com.ufinet.autos.domain.port.out.AutoPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Infrastructure adapter that fulfills the {@link AutoPort} output port
 * using Spring Data JPA. The domain never imports this class directly.
 */
@Component
@RequiredArgsConstructor
public class AutoRepositoryAdapter implements AutoPort {

    private final AutoRepository autoRepository;

    @Override
    public List<Auto> searchByUserId(Long userId, String plate, String brand, Integer year) {
        return autoRepository.searchByUserId(userId, plate, brand, year);
    }

    @Override
    public Optional<Auto> findByIdAndUserId(Long id, Long userId) {
        return autoRepository.findByIdAndUserId(id, userId);
    }

    @Override
    public Auto save(Auto auto) {
        return autoRepository.save(auto);
    }

    @Override
    public void deleteById(Long id) {
        autoRepository.deleteById(id);
    }
}
