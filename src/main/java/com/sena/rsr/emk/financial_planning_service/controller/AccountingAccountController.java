package com.sena.rsr.emk.financial_planning_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sena.rsr.emk.financial_planning_service.model.AccountingAccount;
import com.sena.rsr.emk.financial_planning_service.repository.AccountingAccountRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/accounting-account")
public class AccountingAccountController {

    private final AccountingAccountRepository repository;

    public AccountingAccountController(AccountingAccountRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/user/{userId}")
    public List<AccountingAccount> getAllByUserId(@PathVariable Integer userId) {
        return this.repository.findByUserIdAndIsProjection(userId, true);
    }

    @PostMapping("/")
    public ResponseEntity<AccountingAccount> create(@RequestBody AccountingAccount accountingAccount) {
        AccountingAccount saved = this.repository.save(accountingAccount);
        return new ResponseEntity<AccountingAccount>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountingAccount> update(@PathVariable Integer id,
            @RequestBody AccountingAccount accountingAccount) {

        return repository.findById(id)
                .map(existing -> {
                    existing.setDescription(accountingAccount.getDescription());
                    existing.setUserId(accountingAccount.getUserId());
                    existing.setProjection(accountingAccount.isProjection());

                    AccountingAccount updated = repository.save(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AccountingAccount> delete(@PathVariable Integer id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
