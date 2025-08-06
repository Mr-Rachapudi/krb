package com.krb.backend.service;

import com.krb.backend.dto.AccountDto;
import com.krb.backend.dto.CreateAccountRequest;
import com.krb.backend.entity.Account;
import com.krb.backend.entity.Customer;
import com.krb.backend.entity.Employee;
import com.krb.backend.repository.AccountRepository;
import com.krb.backend.repository.CustomerRepository;
import com.krb.backend.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    public List<AccountDto> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<AccountDto> getAccountById(Long id) {
        return accountRepository.findById(id)
                .map(this::convertToDto);
    }
    
    public Optional<AccountDto> getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .map(this::convertToDto);
    }
    
    public List<AccountDto> getAccountsByCustomer(Long customerId) {
        return accountRepository.findByCustomerIdOrderByCreatedAtDesc(customerId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<AccountDto> getAccountsByEmployee(Long employeeId) {
        return accountRepository.findByCreatedByEmployeeId(employeeId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<AccountDto> getAccountsByType(Account.AccountType accountType) {
        return accountRepository.findByAccountType(accountType).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<AccountDto> getAccountsByStatus(Account.AccountStatus status) {
        return accountRepository.findByStatus(status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public AccountDto createAccount(CreateAccountRequest request, Long employeeId) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        Account account = new Account();
        account.setAccountType(request.getAccountType());
        account.setBalance(request.getInitialBalance());
        account.setCustomer(customer);
        account.setCreatedBy(employee);
        
        if (request.getCreditLimit() != null) {
            account.setCreditLimit(request.getCreditLimit());
        }
        
        Account savedAccount = accountRepository.save(account);
        return convertToDto(savedAccount);
    }
    
    public AccountDto updateAccountStatus(Long id, Account.AccountStatus status) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        
        account.setStatus(status);
        Account savedAccount = accountRepository.save(account);
        return convertToDto(savedAccount);
    }
    
    public AccountDto updateAccountBalance(Long id, BigDecimal newBalance) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Balance cannot be negative");
        }
        
        account.setBalance(newBalance);
        Account savedAccount = accountRepository.save(account);
        return convertToDto(savedAccount);
    }
    
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        
        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new RuntimeException("Cannot delete account with non-zero balance");
        }
        
        accountRepository.delete(account);
    }
    
    public long getAccountCount() {
        return accountRepository.countAccounts();
    }
    
    public BigDecimal getTotalActiveBalance() {
        BigDecimal total = accountRepository.getTotalActiveBalance();
        return total != null ? total : BigDecimal.ZERO;
    }
    
    public List<Object[]> getAccountCountByType() {
        return accountRepository.countAccountsByType();
    }
    
    public List<Object[]> getAccountCountByStatus() {
        return accountRepository.countAccountsByStatus();
    }
    
    private AccountDto convertToDto(Account account) {
        AccountDto dto = new AccountDto();
        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setAccountType(account.getAccountType());
        dto.setBalance(account.getBalance());
        dto.setInterestRate(account.getInterestRate());
        dto.setCreditLimit(account.getCreditLimit());
        dto.setStatus(account.getStatus());
        dto.setCreatedAt(account.getCreatedAt());
        dto.setUpdatedAt(account.getUpdatedAt());
        
        if (account.getCustomer() != null) {
            dto.setCustomerId(account.getCustomer().getId());
            dto.setCustomerName(account.getCustomer().getFullName());
        }
        
        if (account.getCreatedBy() != null) {
            dto.setCreatedByEmployeeId(account.getCreatedBy().getId());
            dto.setCreatedByEmployeeName(account.getCreatedBy().getFullName());
        }
        
        return dto;
    }
}
