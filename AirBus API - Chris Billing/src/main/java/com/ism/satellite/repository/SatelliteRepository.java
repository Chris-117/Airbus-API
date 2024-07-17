package com.ism.satellite.repository;

import com.ism.satellite.domain.Satellite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SatelliteRepository extends JpaRepository<Satellite, Long> {
}
