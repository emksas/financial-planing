package com.sena.rsr.emk.financial_planning_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "planificacionfinanciera", schema = "public")

@Getter
@Setter
@NoArgsConstructor
public class FinancialPlanning {

    @Id
    @Column(name = "idplanificacionfinanciera", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer planId;   // columna: idplanificacionfinanciera

    @Column(name = "usuario_cedula", nullable = false)
    private Integer userId;   // columna: usuario_cedula (FK a usuario.cedula)

    @Column(name = "descripcion", length = 45)
    private String description;   // descripcion

    @Column(name = "nombredelplan", length = 45)
    private String planName;      // nombredelplan

    @Column(name = "valorproyectado")
    private Double projectedValue;   // valorproyectado

    @Column(name = "fechaproyectada", nullable = false)
    private LocalDateTime projectedDate; // fechaproyectada

    @Column(name = "proyectopersonal")
    private Boolean personalProject; // proyectopersonal
}