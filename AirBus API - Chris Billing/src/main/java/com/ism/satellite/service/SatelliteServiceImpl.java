package com.ism.satellite.service;

import com.ism.satellite.domain.Satellite;
import com.ism.satellite.domain.SatelliteParameters;
import com.ism.satellite.exception.custom.BadRequestException;
import com.ism.satellite.exception.custom.NotFoundException;
import com.ism.satellite.repository.SatelliteParametersRepository;
import com.ism.satellite.repository.SatelliteRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SatelliteServiceImpl implements SatelliteService {

    protected final SatelliteRepository repository;

    protected final SatelliteParametersRepository parametersRepository;

    public SatelliteServiceImpl(SatelliteRepository repository, SatelliteParametersRepository parametersRepository) {
        this.repository = repository;
        this.parametersRepository = parametersRepository;
    }

    @Override
    public List<Satellite> getAll() {
        return repository.findAll();
    }

    @Override
    public Satellite getById(Long id) {
        return repository.findById(id)
          .orElseThrow(() -> new NotFoundException(String.format("Satellite with id: %s not found", id)));
    }

    @Override
    public Satellite update(Satellite satellite, Long id) {
        // Efficiently check the existence of satellite otherwise getById() will throw an error.
        getById(id);
        if (satellite.getName() == null ||
          satellite.getOrbit() == null ||
          satellite.getParameters() == null ||
          satellite.getLaunchDate() == null
        ) {
            throw new BadRequestException("Satellite missing required parameters");
        }
        return repository.save(satellite);
    }

    @Override
    public void delete(Long id) {
        getById(id);
        repository.deleteById(id);
    }

    @Override
    public SatelliteParameters getSatellitePosition(Long id) {
        Long parametersId = getById(id).getParameters().getId();
        return parametersRepository.findById(parametersId)
          .orElseThrow(() -> new NotFoundException(String.format("Satellite with id: %s not found", id)));
    }

    @Override
    public Satellite patchParameters(Long satelliteId, SatelliteParameters satelliteParameters) {
        // Validate incoming parameters
        if (satelliteParameters == null ||
          satelliteParameters.getAltitude() == null ||
          satelliteParameters.getLatitude() == null ||
          satelliteParameters.getLongitude() == null) {
            throw new BadRequestException("Satellite Parameters missing required parameters");
        }
        // Get Satellite information
        Satellite satellite = getById(satelliteId);

        // Update Satellite with new values
        satelliteParameters.setId(satellite.getParameters().getId());
        satellite.setParameters(satelliteParameters);

        return satellite;
    }

    @Override
    public Satellite create(Satellite satellite) throws Exception {
        if (satellite.getName() == null ||
          satellite.getOrbit() == null ||
          satellite.getParameters() == null ||
          satellite.getLaunchDate() == null
        ) {
            throw new BadRequestException("Satellite missing required parameters");
        }
        try {
            repository.save(satellite);
        } catch (Exception ex) {
            // Handle generic Exceptions more gracefully in GlobalExceptionHandler
            throw new Exception(ex.getMessage());
        }
        return satellite;
    }
}
