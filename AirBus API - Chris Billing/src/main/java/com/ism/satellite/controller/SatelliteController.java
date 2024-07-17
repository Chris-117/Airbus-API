package com.ism.satellite.controller;

import com.ism.satellite.domain.Satellite;
import com.ism.satellite.domain.SatelliteParameters;
import com.ism.satellite.service.SatelliteService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Satellite Controller Layer. Responses use ResponseEntity to better manage handling of exceptions and errors.
 */
@RestController
@RequestMapping("/api/satellite")
public class SatelliteController {

    @Autowired
    private SatelliteService satelliteService;

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Satellite>> getAllSatellites() {
        return new ResponseEntity<>(satelliteService.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Satellite> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(satelliteService.getById(id), HttpStatus.OK);
    }

    @GetMapping(value = "/position/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SatelliteParameters> getSatellitePosition(@PathVariable("id") Long satelliteId) {
        return new ResponseEntity<>(satelliteService.getSatellitePosition(satelliteId), HttpStatus.OK);
    }

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Satellite> createSatellite(@RequestBody Satellite satellite) throws Exception {
        return new ResponseEntity<>(satelliteService.create(satellite), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Satellite> updateSatellite(@RequestBody Satellite satellite, @PathVariable("id") Long id) {
        return new ResponseEntity<>(satelliteService.update(satellite, id), HttpStatus.OK);
    }

    // Included a Patch method that provides a straight forward way to update a satellites position.
    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Satellite> patchSatelliteParameters(@RequestBody SatelliteParameters satelliteParameters,
                                                              @PathVariable("id") Long satelliteId) {
        return new ResponseEntity<>(satelliteService.patchParameters(satelliteId, satelliteParameters), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        satelliteService.delete(id);
    }
}
