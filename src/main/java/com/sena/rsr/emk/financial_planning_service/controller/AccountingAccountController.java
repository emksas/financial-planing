package com.sena.rsr.emk.financial_planning_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sena.rsr.emk.financial_planning_service.model.AccountingAccount;
import com.sena.rsr.emk.financial_planning_service.repository.AccountingAccountRepository;

@RestController
@RequestMapping("/api/accounting-account")
public class AccountingAccountController {

    private final AccountingAccountRepository repository;

    /* 
    @GetMapping("/user/{userId}")
    public List<AccountingAccount> getAllByUserId( @PathVariable Integer userId ){

    }
    */
}
