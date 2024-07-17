package com.ism.satellite.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * SatelliteParameters Domain object. Could implement the @Builder annotation to assist in constructing the object
 * Modified the naming of the variables as "long" is a reserved word in java
 * Also used no native double data type for further functionality
 */
@Entity
@Getter
@Setter
@Table()
public class SatelliteParameters {
    @Id
    @GeneratedValue(generator = "uuid")
    @Column(name = "id")
    private Long id;
    @NotNull
    private Double altitude;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;

}
