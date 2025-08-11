package com.krb.backend.dto;

import com.krb.backend.entity.Account;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountDto {
    
    private Long id;
    private String accountNumber;
    
    @NotNull(message = "Account type is required")
    private Account.AccountType accountType;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "Balance cannot be negative")
    private BigDecimal balance;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "Interest rate cannot be negative")
    private BigDecimal interestRate;
    
    private BigDecimal creditLimit;
    private Account.AccountStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long customerId;
    private String customerName;
    private Long createdByEmployeeId;
    private String createdByEmployeeName;
    
    public AccountDto() {}
    
    public AccountDto(Long id, String accountNumber, Account.AccountType accountType,
                     BigDecimal balance, BigDecimal interestRate, BigDecimal creditLimit,
                     Account.AccountStatus status, LocalDateTime createdAt, LocalDateTime updatedAt,
                     Long customerId, String customerName, Long createdByEmployeeId, 
                     String createdByEmployeeName) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
        this.interestRate = interestRate;
        this.creditLimit = creditLimit;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.customerId = customerId;
        this.customerName = customerName;
        this.createdByEmployeeId = createdByEmployeeId;
        this.createdByEmployeeName = createdByEmployeeName;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    
    public Account.AccountType getAccountType() { return accountType; }
    public void setAccountType(Account.AccountType accountType) { this.accountType = accountType; }
    
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    
    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
    
    public BigDecimal getCreditLimit() { return creditLimit; }
    public void setCreditLimit(BigDecimal creditLimit) { this.creditLimit = creditLimit; }
    
    public Account.AccountStatus getStatus() { return status; }
    public void setStatus(Account.AccountStatus status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public Long getCreatedByEmployeeId() { return createdByEmployeeId; }
    public void setCreatedByEmployeeId(Long createdByEmployeeId) { this.createdByEmployeeId = createdByEmployeeId; }
    
    public String getCreatedByEmployeeName() { return createdByEmployeeName; }
    public void setCreatedByEmployeeName(String createdByEmployeeName) { this.createdByEmployeeName = createdByEmployeeName; }
    
    public String getAccountTypeDisplayName() {
        return accountType != null ? accountType.getDisplayName() : "";
    }
}
