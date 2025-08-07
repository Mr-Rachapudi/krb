package com.krb.backend.controller;

import com.krb.backend.dto.AccountDto;
import com.krb.backend.dto.CreateAccountRequest;
import com.krb.backend.entity.Account;
import com.krb.backend.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class AccountController {
    
    @Autowired
    private AccountService accountService;
    
    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        List<AccountDto> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long id) {
        Optional<AccountDto> account = accountService.getAccountById(id);
        return account.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<AccountDto> getAccountByAccountNumber(@PathVariable String accountNumber) {
        Optional<AccountDto> account = accountService.getAccountByAccountNumber(accountNumber);
        return account.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountDto>> getAccountsByCustomer(@PathVariable Long customerId) {
        List<AccountDto> accounts = accountService.getAccountsByCustomer(customerId);
        return ResponseEntity.ok(accounts);
    }
    
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AccountDto>> getAccountsByEmployee(@PathVariable Long employeeId) {
        List<AccountDto> accounts = accountService.getAccountsByEmployee(employeeId);
        return ResponseEntity.ok(accounts);
    }
    
    @GetMapping("/type/{accountType}")
    public ResponseEntity<List<AccountDto>> getAccountsByType(@PathVariable Account.AccountType accountType) {
        List<AccountDto> accounts = accountService.getAccountsByType(accountType);
        return ResponseEntity.ok(accounts);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<AccountDto>> getAccountsByStatus(@PathVariable Account.AccountStatus status) {
        List<AccountDto> accounts = accountService.getAccountsByStatus(status);
        return ResponseEntity.ok(accounts);
    }
    
    @PostMapping
    public ResponseEntity<?> createAccount(@Valid @RequestBody CreateAccountRequest request, 
                                         @RequestParam Long employeeId) {
        try {
            AccountDto account = accountService.createAccount(request, employeeId);
            return ResponseEntity.status(HttpStatus.CREATED).body(account);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateAccountStatus(@PathVariable Long id, 
                                               @RequestParam Account.AccountStatus status) {
        try {
            AccountDto updatedAccount = accountService.updateAccountStatus(id, status);
            return ResponseEntity.ok(updatedAccount);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}/balance")
    public ResponseEntity<?> updateAccountBalance(@PathVariable Long id, 
                                                @RequestParam BigDecimal balance) {
        try {
            AccountDto updatedAccount = accountService.updateAccountBalance(id, balance);
            return ResponseEntity.ok(updatedAccount);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        try {
            accountService.deleteAccount(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/count")
    public ResponseEntity<Long> getAccountCount() {
        long count = accountService.getAccountCount();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/total-balance")
    public ResponseEntity<BigDecimal> getTotalActiveBalance() {
        BigDecimal total = accountService.getTotalActiveBalance();
        return ResponseEntity.ok(total);
    }
    
    @GetMapping("/stats/by-type")
    public ResponseEntity<List<Object[]>> getAccountCountByType() {
        List<Object[]> stats = accountService.getAccountCountByType();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/stats/by-status")
    public ResponseEntity<List<Object[]>> getAccountCountByStatus() {
        List<Object[]> stats = accountService.getAccountCountByStatus();
        return ResponseEntity.ok(stats);
    }
}
