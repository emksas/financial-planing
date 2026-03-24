package com.sena.rsr.emk.financial_planning_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.sena.rsr.emk.financial_planning_service.model.FinancialPlanning;
import com.sena.rsr.emk.financial_planning_service.repository.FinancialPlanningRepository;

import java.util.List;

@RestController
@RequestMapping("/api/financial-planning")
public class FinancialPlanningController {

    private final FinancialPlanningRepository repository;

    public FinancialPlanningController(FinancialPlanningRepository repository) {
        this.repository = repository;
    }

    // 🔹 GET /api/financial-planning
    // Lista todo (cuidado si la tabla crece mucho)
    @GetMapping
    public List<FinancialPlanning> getAll() {
        return repository.findAll();
    }

    // 🔹 GET /api/financial-planning/user/{userId}
    // Lista todas las planificaciones de un usuario
    @GetMapping("/user/{userId}")
    public List<FinancialPlanning> getByUser(@PathVariable Integer userId) {
        return repository.findByUserId(userId);
    }

    // 🔹 GET /api/financial-planning/user/{userId}/plan/{planId}
    // Obtiene una planificación específica por PK compuesta
    @GetMapping("/user/{userId}/plan/{planId}")
    public FinancialPlanning getOne(
            @PathVariable Integer userId,
            @PathVariable Integer planId) {

        return repository.findByUserIdAndPlanId(userId, planId);
    }

    // 🔹 POST /api/financial-planning
    // Crea una nueva planificación
    //
    // Espera un JSON con:
    // {
    // "planId": 1,
    // "userId": 123,
    // "description": "...",
    // "planName": "...",
    // "projectedValue": 12345.67,
    // "projectedDate": "2025-12-11T00:00:00",
    // "personalProject": true
    // }
    @PostMapping
    public ResponseEntity<FinancialPlanning> create(@RequestBody FinancialPlanning planning) {

        // Opcional: validar campos obligatorios aquí

        System.out.println(
                "Creando planificación para userId=" + planning.getUserId() + " con planId=" + planning.getPlanId());
        System.out.println("Datos recibidos: " + planning);
        FinancialPlanning saved = repository.save(planning);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // 🔹 PUT /api/financial-planning/user/{userId}/plan/{planId}
    // Actualiza una planificación existente
    @PutMapping("/user/{userId}/plan/{planId}")
    public FinancialPlanning update(
            @PathVariable Integer userId,
            @PathVariable Integer planId,
            @RequestBody FinancialPlanning updatedData) {

        FinancialPlanning existing = repository.findByUserIdAndPlanId(userId, planId);

        // Solo actualizamos los campos que tenga sentido modificar
        existing.setDescription(updatedData.getDescription());
        existing.setPlanName(updatedData.getPlanName());
        existing.setProjectedValue(updatedData.getProjectedValue());
        existing.setProjectedDate(updatedData.getProjectedDate());
        existing.setPersonalProject(updatedData.getPersonalProject());

        return repository.save(existing);
    }

    // 🔹 DELETE /api/financial-planning/user/{userId}/plan/{planId}
    // Elimina una planificación
    @DeleteMapping("/user/{userId}/plan/{planId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Integer userId,
            @PathVariable Integer planId) {

        FinancialPlanning planning = repository.findByUserIdAndPlanId(userId, planId);
        if (planning == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Financial planning not found");
        }

        repository.delete(planning);
    }

}
