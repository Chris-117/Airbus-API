package com.ism.satellite.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ism.satellite.domain.OrbitType;
import com.ism.satellite.domain.Satellite;
import com.ism.satellite.domain.SatelliteParameters;
import com.ism.satellite.repository.SatelliteParametersRepository;
import com.ism.satellite.repository.SatelliteRepository;
import com.ism.satellite.service.SatelliteService;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Integration tests for the expected behaviour of the Satellite API.
 */
@WebMvcTest
public class SatelliteControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private SatelliteRepository satelliteRepository;

    @MockBean
    private SatelliteParametersRepository parametersRepository;

    @MockBean
    public SatelliteService satelliteService;

    @MockBean
    SatelliteRepository repository;

    private Satellite satellite;
    private SatelliteParameters satelliteParameters;

    String url = "/api/satellite/";

    @BeforeEach
    void setup() {
        // Create sample SatelliteParameters
        satelliteParameters = new SatelliteParameters();
        satelliteParameters.setAltitude(122.50);
        satelliteParameters.setLatitude(322.67);
        satelliteParameters.setLongitude(722.93);

        // Create sample Satellite
        satellite = new Satellite();
        satellite.setId(1L);
        satellite.setName("Death Star");
        satellite.setOrbit(OrbitType.MEO);
        satellite.setParameters(satelliteParameters);
    }

    @Test
    void getAllSatellites() throws Exception {
        when(satelliteService.getAll()).thenReturn(Arrays.asList(satellite));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/satellite/")
            .accept(MediaType.APPLICATION_JSON))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(satellite.getId()))
          .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(satellite.getName()))
          .andExpect(MockMvcResultMatchers.jsonPath("$[0].orbit").value(satellite.getOrbit().toString()));
    }

    @Test
    void getSatellitePosition_Success() throws Exception {
        when(satelliteService.getSatellitePosition(anyLong())).thenReturn(satelliteParameters);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/satellite/position/{id}", 1L)
            .accept(MediaType.APPLICATION_JSON))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(MockMvcResultMatchers.jsonPath("$.altitude").value(satelliteParameters.getAltitude()))
          .andExpect(MockMvcResultMatchers.jsonPath("$.latitude").value(satelliteParameters.getLatitude()))
          .andExpect(MockMvcResultMatchers.jsonPath("$.longitude").value(satelliteParameters.getLongitude()));
    }

    @Test
    void getById_Success() throws Exception {
        when(satelliteService.getById(anyLong())).thenReturn(satellite);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/satellite/{id}", 1L)
            .accept(MediaType.APPLICATION_JSON))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(satellite.getId()))
          .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(satellite.getName()))
          .andExpect(MockMvcResultMatchers.jsonPath("$.orbit").value(satellite.getOrbit().toString()));
    }

    @Test
    void createSatellite_Success() throws Exception {
        when(satelliteService.create(any(Satellite.class))).thenReturn(satellite);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/satellite/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(satellite))
            .accept(MediaType.APPLICATION_JSON))
          .andExpect(MockMvcResultMatchers.status().isCreated())
          .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(satellite.getId()))
          .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(satellite.getName()))
          .andExpect(MockMvcResultMatchers.jsonPath("$.orbit").value(satellite.getOrbit().toString()));
    }

    @Test
    void updateSatellite_Success() throws Exception {
        when(satelliteService.update(any(Satellite.class), anyLong())).thenReturn(satellite);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/satellite/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(satellite))
            .accept(MediaType.APPLICATION_JSON))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(satellite.getId()))
          .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(satellite.getName()))
          .andExpect(MockMvcResultMatchers.jsonPath("$.orbit").value(satellite.getOrbit().toString()));
    }

    @Test
    void delete_Success() throws Exception {
        doNothing().when(satelliteService).delete(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/satellite/{id}", 1L))
          .andExpect(MockMvcResultMatchers.status().isNoContent());
    }



}
