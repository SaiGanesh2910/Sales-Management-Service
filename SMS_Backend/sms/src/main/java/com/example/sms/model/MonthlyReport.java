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
@Table(name = "monthly_report")
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class MonthlyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="month")
    private String month;

    @Column(name = "year")
    private String year;

    @Column(name="email")
    private String email;

    @Column(name = "commission_value")
    private long commissionValue;

    @Column(name="sales_value")
    private long salesValue;
}
