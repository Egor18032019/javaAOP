package ru.t1.java.demo.model;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MetricModel {
    private UUID id;
    private long executionTime;
    private String methodName;
    private String methodParameters;
}
