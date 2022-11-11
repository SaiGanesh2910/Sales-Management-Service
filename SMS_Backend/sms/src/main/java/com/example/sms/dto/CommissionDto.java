package com.example.sms.dto;

import com.example.sms.miscellaneous.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommissionDto {
    private int id;
    private int vehicleId;
    private long minRange;
    private long maxRange;
    private int percentage;
}
