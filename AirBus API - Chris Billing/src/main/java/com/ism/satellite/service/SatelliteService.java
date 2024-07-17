package com.ism.satellite.service;

import com.ism.satellite.domain.Satellite;
import com.ism.satellite.domain.SatelliteParameters;
import java.util.List;

public interface SatelliteService {
    List<Satellite> getAll();

    Satellite create(Satellite satellite) throws Exception;

    Satellite getById(Long id);

    Satellite update(Satellite satellite, Long id);

    void delete(Long id);

    SatelliteParameters getSatellitePosition(Long id);

    Satellite patchParameters(Long satelliteId, SatelliteParameters satelliteParameters);
}
