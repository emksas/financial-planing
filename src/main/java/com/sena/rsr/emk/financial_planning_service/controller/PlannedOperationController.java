package com.sena.rsr.emk.financial_planning_service.controller;

import com.sena.rsr.emk.financial_planning_service.model.PlannedOperation;
import com.sena.rsr.emk.financial_planning_service.repository.PlannedOperationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/planned-operations")
public class PlannedOperationController {
    private final PlannedOperationRepository repository;

    public PlannedOperationController(PlannedOperationRepository repository) {
        this.repository = repository;
    }

    // 🔹 GET /api/planned-operations/user/{userId}/plan/{planId}
    // Lista todas las operaciones planificadas de un usuario para una planificación
    @GetMapping("/user/{userId}/plan/{planId}")
    public List<PlannedOperation> getByUserAndPlan(
            @PathVariable Integer userId,
            @PathVariable("planId") Integer planificationId) {
        return repository.findByUserIdAndPlanificationId(userId, planificationId);
    }

    // 🔹 GET
    // /api/planned-operations/user/{userId}/plan/{planId}/operation/{operationId}
    // Obtiene una operación específica, validando que pertenezca al usuario +
    // planificación
    @GetMapping("/user/{userId}/plan/{planId}/operation/{operationId}")
    public PlannedOperation getOne(
            @PathVariable Integer userId,
            @PathVariable("planId") Integer planificationId,
            @PathVariable Integer operationId) {
        return repository.findByIdAndUserIdAndPlanificationId(operationId, userId, planificationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Planned operation not found"));
    }

    // 🔹 POST /api/planned-operations/user/{userId}/plan/{planId}
    // Crea una nueva operación para ese usuario y esa planificación
    //
    // Body (JSON, sin necesidad de enviar userId ni planificationId):
    // {
    // "description": "...",
    // "accountId": 10,
    // "dueDate": "2025-12-31",
    // "repetitive": false,
    // "projectedValue": 1500000
    // }
    @PostMapping("/user/{userId}/plan/{planId}")
    public ResponseEntity<PlannedOperation> create(
            @PathVariable Integer userId,
            @PathVariable("planId") Integer planificationId,
            @RequestBody PlannedOperation input) {
        PlannedOperation operation = new PlannedOperation();

        // Forzamos siempre userId y planificationId desde la ruta
        operation.setUserId(userId);
        operation.setPlanificationId(planificationId);

        // Campos que vienen del body
        operation.setDescription(input.getDescription());
        operation.setAccountId(input.getAccountId());
        //operation.setDueDate(input.getDueDate());
        operation.setProjectedValue(input.getProjectedValue());
        operation.setAmount(input.getAmount());
        operation.setTotalProjectedValue(input.getTotalProjectedValue());

        System.out.println("Planned operation: ");
        System.out.println(operation);

        // Fecha de creación: si no viene, usamos hoy
        if (input.getCreationDate() != null) {
            operation.setCreationDate(input.getCreationDate());
        } else {
            operation.setCreationDate(LocalDate.now());
        }

        // Campo obligatorio (según tu tabla tiene default false)
        operation.setRepetitive(input.getRepetitive() != null ? input.getRepetitive() : Boolean.FALSE);

        PlannedOperation saved = repository.save(operation);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // 🔹 PUT
    // /api/planned-operations/user/{userId}/plan/{planId}/operation/{operationId}
    // Actualiza una operación existente, validando user + plan
    @PutMapping("/user/{userId}/plan/{planId}/operation/{operationId}")
    public PlannedOperation update(
            @PathVariable Integer userId,
            @PathVariable("planId") Integer planificationId,
            @PathVariable Integer operationId,
            @RequestBody PlannedOperation input) {
        PlannedOperation existing = repository
                .findByIdAndUserIdAndPlanificationId(operationId, userId, planificationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Planned operation not found"));

        // NO cambiamos userId ni planificationId (se mantienen consistentes)
        existing.setDescription(input.getDescription());
        existing.setAccountId(input.getAccountId());
        existing.setDueDate(input.getDueDate());
        existing.setProjectedValue(input.getProjectedValue());

        if (input.getRepetitive() != null) {
            existing.setRepetitive(input.getRepetitive());
        }

        // Si quieres permitir cambiar la date_creation (no suele ser lo normal):
        if (input.getCreationDate() != null) {
            existing.setCreationDate(input.getCreationDate());
        }

        return repository.save(existing);
    }

    // 🔹 DELETE
    // /api/planned-operations/user/{userId}/plan/{planId}/operation/{operationId}
    // Elimina una operación, validando que pertenezca a ese user + plan
    @DeleteMapping("/user/{userId}/plan/{planId}/operation/{operationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Integer userId,
            @PathVariable("planId") Integer planificationId,
            @PathVariable Integer operationId) {
        PlannedOperation existing = repository
                .findByIdAndUserIdAndPlanificationId(operationId, userId, planificationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Planned operation not found"));

        repository.delete(existing);
    }
}
