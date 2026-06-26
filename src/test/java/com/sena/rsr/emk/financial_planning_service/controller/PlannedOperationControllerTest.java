package com.sena.rsr.emk.financial_planning_service.controller;

import com.sena.rsr.emk.financial_planning_service.model.PlannedOperation;
import com.sena.rsr.emk.financial_planning_service.repository.PlannedOperationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlannedOperationControllerTest {

    @Mock
    private PlannedOperationRepository repository;

    @InjectMocks
    private PlannedOperationController controller;

    @Test
    void getByUserAndPlanReturnsOperations() {
        PlannedOperation operation = operation(1, 10, 20, "Cuota");
        when(repository.findByUserIdAndPlanificationId(10, 20)).thenReturn(List.of(operation));

        List<PlannedOperation> result = controller.getByUserAndPlan(10, 20);

        assertEquals(List.of(operation), result);
    }

    @Test
    void getOneReturnsOperationWhenItExists() {
        PlannedOperation operation = operation(1, 10, 20, "Cuota");
        when(repository.findByIdAndUserIdAndPlanificationId(1, 10, 20)).thenReturn(Optional.of(operation));

        PlannedOperation result = controller.getOne(10, 20, 1);

        assertSame(operation, result);
    }

    @Test
    void getOneThrowsNotFoundWhenOperationDoesNotExist() {
        when(repository.findByIdAndUserIdAndPlanificationId(404, 10, 20)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> controller.getOne(10, 20, 404));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void createUsesUserAndPlanFromPathAndDefaultsMissingFields() {
        PlannedOperation input = operation(null, 999, 999, "Nueva");
        input.setCreationDate(null);
        input.setRepetitive(null);
        PlannedOperation saved = operation(2, 10, 20, "Nueva");
        when(repository.save(org.mockito.ArgumentMatchers.any(PlannedOperation.class))).thenReturn(saved);

        ResponseEntity<PlannedOperation> response = controller.create(10, 20, input);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(saved, response.getBody());

        ArgumentCaptor<PlannedOperation> captor = ArgumentCaptor.forClass(PlannedOperation.class);
        verify(repository).save(captor.capture());
        PlannedOperation created = captor.getValue();
        assertEquals(10, created.getUserId());
        assertEquals(20, created.getPlanificationId());
        assertEquals(LocalDate.now(), created.getCreationDate());
        assertFalse(created.getRepetitive());
    }

    @Test
    void updateReturnsUpdatedOperationWhenItExists() {
        PlannedOperation existing = operation(3, 10, 20, "Vieja");
        PlannedOperation input = operation(null, 10, 20, "Actualizada");
        input.setProjectedValue(500.0);
        when(repository.findByIdAndUserIdAndPlanificationId(3, 10, 20)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        PlannedOperation result = controller.update(10, 20, 3, input);

        assertSame(existing, result);
        assertEquals("Actualizada", result.getDescription());
        assertEquals(500.0, result.getProjectedValue());
    }

    @Test
    void updateThrowsNotFoundWhenOperationDoesNotExist() {
        when(repository.findByIdAndUserIdAndPlanificationId(404, 10, 20)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> controller.update(10, 20, 404, operation(null, 10, 20, "Actualizada")));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void deleteRemovesOperationWhenItExists() {
        PlannedOperation existing = operation(4, 10, 20, "Eliminar");
        when(repository.findByIdAndUserIdAndPlanificationId(4, 10, 20)).thenReturn(Optional.of(existing));

        controller.delete(10, 20, 4);

        verify(repository).delete(existing);
    }

    @Test
    void deleteThrowsNotFoundWhenOperationDoesNotExist() {
        when(repository.findByIdAndUserIdAndPlanificationId(404, 10, 20)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> controller.delete(10, 20, 404));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    private PlannedOperation operation(Integer id, Integer userId, Integer planificationId, String description) {
        PlannedOperation operation = new PlannedOperation();
        operation.setId(id);
        operation.setUserId(userId);
        operation.setPlanificationId(planificationId);
        operation.setDescription(description);
        operation.setAccountId(7);
        operation.setCreationDate(LocalDate.of(2026, 6, 1));
        operation.setDueDate(LocalDate.of(2026, 7, 1));
        operation.setRepetitive(false);
        operation.setProjectedValue(100.0);
        operation.setAmount(2);
        operation.setTotalProjectedValue(200.0);
        return operation;
    }
}
