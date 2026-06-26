package com.sena.rsr.emk.financial_planning_service.controller;

import com.sena.rsr.emk.financial_planning_service.model.FinancialPlanning;
import com.sena.rsr.emk.financial_planning_service.repository.FinancialPlanningRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinancialPlanningControllerTest {

    @Mock
    private FinancialPlanningRepository repository;

    @InjectMocks
    private FinancialPlanningController controller;

    @Test
    void getAllReturnsPlans() {
        FinancialPlanning plan = plan(1, 10, "Casa", "Plan casa");
        when(repository.findAll()).thenReturn(List.of(plan));

        List<FinancialPlanning> result = controller.getAll();

        assertEquals(List.of(plan), result);
    }

    @Test
    void getByUserReturnsPlansForUser() {
        FinancialPlanning plan = plan(2, 10, "Viaje", "Plan viaje");
        when(repository.findByUserId(10)).thenReturn(List.of(plan));

        List<FinancialPlanning> result = controller.getByUser(10);

        assertEquals(List.of(plan), result);
    }

    @Test
    void getOneReturnsSelectedPlan() {
        FinancialPlanning plan = plan(3, 10, "Estudio", "Plan estudio");
        when(repository.findByUserIdAndPlanId(10, 3)).thenReturn(plan);

        FinancialPlanning result = controller.getOne(10, 3);

        assertSame(plan, result);
    }

    @Test
    void createReturnsCreatedPlan() {
        FinancialPlanning input = plan(null, 10, "Nuevo", "Plan nuevo");
        FinancialPlanning saved = plan(4, 10, "Nuevo", "Plan nuevo");
        when(repository.save(input)).thenReturn(saved);

        ResponseEntity<FinancialPlanning> response = controller.create(input);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(saved, response.getBody());
    }

    @Test
    void updateReturnsUpdatedPlan() {
        FinancialPlanning existing = plan(5, 10, "Viejo", "Plan viejo");
        FinancialPlanning input = plan(null, 10, "Actualizado", "Plan actualizado");
        input.setProjectedValue(900.0);
        when(repository.findByUserIdAndPlanId(10, 5)).thenReturn(existing);
        when(repository.save(existing)).thenReturn(existing);

        FinancialPlanning result = controller.update(10, 5, input);

        assertSame(existing, result);
        assertEquals("Actualizado", result.getDescription());
        assertEquals("Plan actualizado", result.getPlanName());
        assertEquals(900.0, result.getProjectedValue());
    }

    @Test
    void deleteRemovesPlanWhenItExists() {
        FinancialPlanning existing = plan(6, 10, "Eliminar", "Plan eliminar");
        when(repository.findByUserIdAndPlanId(10, 6)).thenReturn(existing);

        controller.delete(10, 6);

        verify(repository).delete(existing);
    }

    @Test
    void deleteThrowsNotFoundWhenPlanDoesNotExist() {
        when(repository.findByUserIdAndPlanId(10, 404)).thenReturn(null);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> controller.delete(10, 404));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    private FinancialPlanning plan(Integer planId, Integer userId, String description, String planName) {
        FinancialPlanning plan = new FinancialPlanning();
        plan.setPlanId(planId);
        plan.setUserId(userId);
        plan.setDescription(description);
        plan.setPlanName(planName);
        plan.setProjectedValue(100.0);
        plan.setProjectedDate(LocalDateTime.of(2026, 6, 1, 12, 0));
        plan.setPersonalProject(true);
        return plan;
    }
}
