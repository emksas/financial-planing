package com.sena.rsr.emk.financial_planning_service.repository;

import com.sena.rsr.emk.financial_planning_service.model.FinancialPlanning;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialPlanningRepository extends JpaRepository<FinancialPlanning, Long> {
     List<FinancialPlanning> findByUserId(Integer userId);

     FinancialPlanning findByUserIdAndPlanId(Integer userId, Integer planId);

     FinancialPlanning findByPlanId(Integer planId);
}
