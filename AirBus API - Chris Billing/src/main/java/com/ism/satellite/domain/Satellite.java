package com.ism.satellite.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Satellite Domain object. Could implement the @Builder annotation to assist in constructing the object
 */

@Entity
@Getter
@Setter
@Data
@Table(name = "satellite")
public class Satellite  {

    @Id
    @GeneratedValue(generator = "uuid")
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Satellite must have a launch date")
    private LocalDateTime launchDate;

    @NotNull(message = "Name is required for Satellite")
    @Size(max = 50, message = "Name must be less than 50 characters")
    private String name;

    @NotNull(message = "Orbit is required for Satellite consisting of GEO, MEO, LEO")
    @Enumerated(EnumType.STRING)
    private OrbitType orbit;

    @NotNull(message = "Satellite must have Parameters")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "param_id", referencedColumnName = "id")
    private SatelliteParameters parameters;

}
