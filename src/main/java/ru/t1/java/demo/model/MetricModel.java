package ru.t1.java.demo.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MetricModel {
    private UUID id;
    private double operatingTime;
    private String methodName;
    private String methodParameters;

    private LocalDateTime timestamp;
}
