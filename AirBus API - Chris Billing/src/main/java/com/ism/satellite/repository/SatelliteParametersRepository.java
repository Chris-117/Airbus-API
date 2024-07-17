package com.ism.satellite.repository;

import com.ism.satellite.domain.SatelliteParameters;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SatelliteParametersRepository extends JpaRepository<SatelliteParameters, Long> {
}
