package com.ufinet.autos.domain.service;

import com.ufinet.autos.domain.model.Auto;
import com.ufinet.autos.domain.port.in.AutoService;
import com.ufinet.autos.domain.port.out.AutoPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Domain service implementing car use cases.
 * Imports only domain types — zero framework infrastructure (no JPA, no Jackson, no EntityManager).
 */
@Service
@RequiredArgsConstructor
public class AutoServiceImpl implements AutoService {

    private final AutoPort autoPort;

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public List<Auto> searchAutos(Long userId, String plate, String brand, Integer year) {
        return autoPort.searchByUserId(userId, plate, brand, year);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Optional<Auto> getAutoByIdAndUserId(Long id, Long userId) {
        return autoPort.findByIdAndUserId(id, userId);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public Auto createAuto(Auto auto) {
        return autoPort.save(auto);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public Auto updateAuto(Auto auto) {
        return autoPort.save(auto);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public void deleteAuto(Long id) {
        autoPort.deleteById(id);
    }
}
