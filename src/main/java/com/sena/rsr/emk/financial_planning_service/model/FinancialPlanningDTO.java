package com.sena.rsr.emk.financial_planning_service.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FinancialPlanningDTO {

    private Integer planId;
    private Integer userId;
    private String description;
    private String planName;
    private Double projectedValue;
    private String projectedDate; // Cambiado a String para recibir la fecha en formato ISO 8601
    private Boolean personalProject;
    private List<PlannedOperation> plannedOperations; // Lista de operaciones planificadas asociadas a esta planificación

}
