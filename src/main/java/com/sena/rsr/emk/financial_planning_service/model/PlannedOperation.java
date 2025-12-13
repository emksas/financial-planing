package com.sena.rsr.emk.financial_planning_service.model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "operations_planified", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class PlannedOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;  // id

    @Column(name = "id_planification")
    private Integer planificationId; // id_planification (relación lógica con planificacionfinanciera)

    @Column(name = "date_creation", nullable = false)
    private LocalDate creationDate; // date_creation

    @Column(name = "date_to_pay")
    private LocalDate dueDate; // date_to_pay

    @Column(name = "description")
    private String description; // description

    @Column(name = "account_id")
    private Integer accountId; // account_id

    @Column(name = "user_id")
    private Integer userId; // user_id

    @Column(name = "\"isRepetitive\"", nullable = false)
    private Boolean repetitive; // "isRepetitive"

    @Column(name = "projected_unit_value")
    private Double projectedValue; // projected_value

    @Column(name = "amount")
    private Integer amount; // amount

    @Column(name = "total_projected_value")
    private Double totalProjectedValue; // total_projected_value
}
