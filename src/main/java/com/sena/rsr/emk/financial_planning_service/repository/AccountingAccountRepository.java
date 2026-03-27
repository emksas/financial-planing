package com.sena.rsr.emk.financial_planning_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sena.rsr.emk.financial_planning_service.model.AccountingAccount;

public interface AccountingAccountRepository extends JpaRepository<AccountingAccount, Integer> {

    List<AccountingAccount> findByUserIdAnd

}
