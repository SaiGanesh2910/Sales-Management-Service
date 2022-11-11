package com.example.sms.dto;

import com.example.sms.miscellaneous.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SalespersonDto {
    private int id;
    private String name;
    private String email;
    private String password;
    private String branch;
    private long contact;
    private Status status;
    private String state;
    private long monthlySales;
    private long commission;
    private int superiorId;
}
