package com.sena.rsr.emk.financial_planning_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity

@Table(name = "cuentacontable", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class AccountingAccount {

    @Id
    @Column(name = "idcuentacontable", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "descripcion")
    private String description;

    @Column(name = "userId")
    private Integer userId; 
    
    @Column(name="esProyeccion")
    private boolean isProjection; 
}
