package com.sena.rsr.emk.financial_planning_service.controller;

import com.sena.rsr.emk.financial_planning_service.model.AccountingAccount;
import com.sena.rsr.emk.financial_planning_service.repository.AccountingAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountingAccountControllerTest {

    @Mock
    private AccountingAccountRepository repository;

    @InjectMocks
    private AccountingAccountController controller;

    @Test
    void getAllByUserIdReturnsProjectionAccounts() {
        AccountingAccount account = account(1, "Ahorro", 10, true);
        when(repository.findByUserIdAndIsProjection(10, true)).thenReturn(List.of(account));

        List<AccountingAccount> result = controller.getAllByUserId(10);

        assertEquals(List.of(account), result);
    }

    @Test
    void createReturnsCreatedAccount() {
        AccountingAccount input = account(null, "Inversion", 20, true);
        AccountingAccount saved = account(2, "Inversion", 20, true);
        when(repository.save(input)).thenReturn(saved);

        ResponseEntity<AccountingAccount> response = controller.create(input);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(saved, response.getBody());
    }

    @Test
    void updateReturnsUpdatedAccountWhenItExists() {
        AccountingAccount existing = account(3, "Vieja", 30, false);
        AccountingAccount input = account(null, "Nueva", 31, true);
        when(repository.findById(3)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        ResponseEntity<AccountingAccount> response = controller.update(3, input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Nueva", response.getBody().getDescription());
        assertEquals(31, response.getBody().getUserId());
        assertEquals(true, response.getBody().isProjection());
    }

    @Test
    void updateReturnsNotFoundWhenAccountDoesNotExist() {
        when(repository.findById(404)).thenReturn(Optional.empty());

        ResponseEntity<AccountingAccount> response = controller.update(404, account(null, "Nueva", 31, true));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteReturnsNoContentWhenAccountExists() {
        when(repository.existsById(5)).thenReturn(true);

        ResponseEntity<AccountingAccount> response = controller.delete(5);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(repository).deleteById(5);
    }

    @Test
    void deleteReturnsNotFoundWhenAccountDoesNotExist() {
        when(repository.existsById(404)).thenReturn(false);

        ResponseEntity<AccountingAccount> response = controller.delete(404);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private AccountingAccount account(Integer id, String description, Integer userId, boolean projection) {
        AccountingAccount account = new AccountingAccount();
        account.setId(id);
        account.setDescription(description);
        account.setUserId(userId);
        account.setProjection(projection);
        return account;
    }
}
