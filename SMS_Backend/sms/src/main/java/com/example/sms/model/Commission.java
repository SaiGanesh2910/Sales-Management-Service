package com.example.sms.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@Builder
@Table(name = "commission")
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class Commission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "vehicle_id")
    private int vehicleId;

    @Column(name="min_range")
    private long minRange;

    @Column(name = "max_range")
    private long maxRange;

    @Column(name = "percentage")
    private int percentage;

}
