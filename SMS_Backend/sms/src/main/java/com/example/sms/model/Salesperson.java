package com.example.sms.model;

import com.example.sms.miscellaneous.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@Builder
@Table(name = "salesperson")
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class Salesperson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name="email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "branch")
    private String branch;

    @Column(name = "contact")
    private long contact;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "state")
    private String state;

    @Column(name = "monthly_sales")
    private long monthlySales;

    @Column(name = "commission_value")
    private long commission;

    @Column(name = "superior_id")
    private int superiorId;
}
