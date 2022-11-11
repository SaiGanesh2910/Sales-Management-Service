package com.example.sms.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@Builder
@Table(name = "sales_csv")
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class SalesCsv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "salesperson_id")
    private int salespersonId;

    @Column(name="cost")
    private long cost;

    @Column(name = "date")
    private Date date;

    @Column(name = "vehicle_id")
    private int vehicleId;
}
