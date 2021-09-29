package ch.globaz.common.ws;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HealthDto {
    private LocalDateTime upSince;
    private String state;
    private String service;
}
