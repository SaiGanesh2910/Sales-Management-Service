package com.example.sms.dto;

import com.example.sms.miscellaneous.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SalespersonReturnDto {
    private int id;
    private String name;
    private String email;
    private String branch;
    private long contact;
    private String state;
    private long monthlySales;
    private long commission;
    private int superiorId;
}
