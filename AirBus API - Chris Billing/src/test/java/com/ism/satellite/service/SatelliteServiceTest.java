package com.ism.satellite.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import com.ism.satellite.domain.OrbitType;
import com.ism.satellite.domain.Satellite;
import com.ism.satellite.domain.SatelliteParameters;
import com.ism.satellite.exception.custom.BadRequestException;
import com.ism.satellite.exception.custom.NotFoundException;
import com.ism.satellite.repository.SatelliteParametersRepository;
import com.ism.satellite.repository.SatelliteRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * Unit tests for the expected I/O of the Satellite service layer.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class SatelliteServiceTest {

    @Autowired
    SatelliteRepository repository;

    @Autowired
    private TestEntityManager em;

    @Autowired
    SatelliteParametersRepository parametersRepository;

    private SatelliteServiceImpl satelliteService;

    private Satellite satellite;
    private SatelliteParameters satelliteParameters;

    // Common functionality to run before each test
    @BeforeEach
    void setUp() {
        satelliteService = new SatelliteServiceImpl(repository, parametersRepository);

        // Create and persist sample Satellite Parameters
        satelliteParameters = new SatelliteParameters();
        satelliteParameters.setAltitude(122.50);
        satelliteParameters.setLatitude(322.67);
        satelliteParameters.setLongitude(722.93);

        // Persist the satellite parameters first
        satelliteParameters = em.persistAndFlush(satelliteParameters);

        // Create and persist sample Satellite
        satellite = new Satellite();
        satellite.setName("Death Star");
        satellite.setLaunchDate(LocalDateTime.now().plusDays(1));
        satellite.setOrbit(OrbitType.MEO);
        satellite.setParameters(satelliteParameters);

        // Persist the satellite entity
        satellite = em.persistAndFlush(satellite);
    }

    @Test
    void getAll() {
        List<Satellite> satellites = satelliteService.getAll();

        assertNotNull(satellites);
        assertFalse(satellites.isEmpty());
        assertEquals(satellites.get(0).getName(), satellite.getName());
    }

    @Test
    void getById_Success() {
        Satellite foundSatellite = satelliteService.getById(satellite.getId());

        assertEquals(satellite, foundSatellite);
    }

    @Test
    void getById_NotFound() {
        NotFoundException ex = assertThrows(NotFoundException.class, () -> satelliteService.getById(99L));

        assertEquals(ex.getMessage(), "Satellite with id: 99 not found");
    }

    @Test
    void getSatellitePosition_Success() {
        SatelliteParameters foundParameters = satelliteService.getSatellitePosition(satellite.getId());

        assertNotNull(foundParameters);
        assertEquals(satelliteParameters, foundParameters);
    }

    @Test
    void getSatellitePosition_NotFound() {
        NotFoundException ex = assertThrows(NotFoundException.class, () -> satelliteService.getSatellitePosition(99L));

        assertEquals(ex.getMessage(), "Satellite with id: 99 not found");
    }

    @Test
    void update_Success() {
        Satellite updatedSatellite = new Satellite();
        updatedSatellite.setId(satellite.getId()); // Use the same ID
        updatedSatellite.setName("Death Star 2");
        updatedSatellite.setOrbit(OrbitType.GEO);
        updatedSatellite.setParameters(satelliteParameters);
        updatedSatellite.setLaunchDate(LocalDateTime.now().plusDays(1));

        satelliteService.update(updatedSatellite, satellite.getId());

        Satellite newSatellite = satelliteService.getById(satellite.getId());

        assertEquals("Death Star 2", newSatellite.getName());
        assertEquals(OrbitType.GEO, newSatellite.getOrbit());
    }

    @Test
    void update_NotFound() {
        Satellite updatedSatellite = new Satellite();
        updatedSatellite.setId(99L); // Non-existent ID
        updatedSatellite.setName("Death Star 2");
        updatedSatellite.setOrbit(OrbitType.GEO);
        updatedSatellite.setParameters(satelliteParameters);
        updatedSatellite.setLaunchDate(LocalDateTime.now().plusDays(1));

        NotFoundException ex = assertThrows(NotFoundException.class, () -> satelliteService.update(updatedSatellite, 99L));

        assertEquals(ex.getMessage(), "Satellite with id: 99 not found");
    }

    @Test
    void update_BadRequest() {
        Satellite existingSatellite = new Satellite();
        existingSatellite.setId(satellite.getId());
        existingSatellite.setName("Death Star 2");
        // Missing orbit and launch date field, which is required
        existingSatellite.setParameters(satelliteParameters);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> satelliteService.update(existingSatellite, satellite.getId()));

        assertEquals("Satellite missing required parameters", ex.getMessage());
    }

    @Test
    void delete_Success() {
        satelliteService.delete(satellite.getId());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> satelliteService.getById(satellite.getId()));
        assertEquals("Satellite with id: " + satellite.getId() + " not found", ex.getMessage());
    }


    @Test
    void delete_NotFound() {
        NotFoundException ex = assertThrows(NotFoundException.class, () -> satelliteService.delete(99L));

        assertEquals(ex.getMessage(), "Satellite with id: 99 not found");
    }

    @Test
    void create_Success() throws Exception {
        Satellite newSatellite = new Satellite();
        newSatellite.setName("Death Star 2");
        newSatellite.setOrbit(OrbitType.LEO);
        newSatellite.setParameters(satelliteParameters);
        newSatellite.setLaunchDate(LocalDateTime.now().plusDays(1));

        Satellite createdSatellite = satelliteService.create(newSatellite);

        assertNotNull(createdSatellite.getId());
        assertEquals("Death Star 2", createdSatellite.getName());
        assertEquals(OrbitType.LEO, createdSatellite.getOrbit());
    }

    @Test
    void create_BadRequest() {
        Satellite newSatellite = new Satellite();
        newSatellite.setOrbit(OrbitType.LEO);
        newSatellite.setParameters(satelliteParameters);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> satelliteService.create(newSatellite));

        assertEquals(ex.getMessage(), "Satellite missing required parameters");
    }

}
