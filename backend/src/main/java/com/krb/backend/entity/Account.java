package com.krb.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
public class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String accountNumber;
    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Account type is required")
    private AccountType accountType;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "Balance cannot be negative")
    @Column(precision = 15, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "Interest rate cannot be negative")
    @Column(precision = 5, scale = 2)
    private BigDecimal interestRate = BigDecimal.ZERO;
    
    @Column(precision = 15, scale = 2)
    private BigDecimal creditLimit;
    
    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.ACTIVE;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_employee_id")
    private Employee createdBy;
    
    public enum AccountType {
        SAVINGS("Savings Account", 2.5),
        CHECKING("Checking Account", 0.1),
        FIXED_DEPOSIT("Fixed Deposit Account", 4.5),
        CREDIT_CARD("Credit Card Account", 18.9),
        MONEY_MARKET("Money Market Account", 3.2),
        BUSINESS_CHECKING("Business Checking Account", 0.5);
        
        private final String displayName;
        private final double defaultInterestRate;
        
        AccountType(String displayName, double defaultInterestRate) {
            this.displayName = displayName;
            this.defaultInterestRate = defaultInterestRate;
        }
        
        public String getDisplayName() { return displayName; }
        public double getDefaultInterestRate() { return defaultInterestRate; }
    }
    
    public enum AccountStatus {
        ACTIVE, INACTIVE, CLOSED, SUSPENDED
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (accountNumber == null) {
            generateAccountNumber();
        }
        if (interestRate.equals(BigDecimal.ZERO) && accountType != null) {
            interestRate = BigDecimal.valueOf(accountType.getDefaultInterestRate());
        }
        if (accountType == AccountType.CREDIT_CARD && creditLimit == null) {
            creditLimit = BigDecimal.valueOf(5000.00);
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    private void generateAccountNumber() {
        String prefix = switch (accountType) {
            case SAVINGS -> "SAV";
            case CHECKING -> "CHK";
            case FIXED_DEPOSIT -> "FD";
            case CREDIT_CARD -> "CC";
            case MONEY_MARKET -> "MM";
            case BUSINESS_CHECKING -> "BC";
        };
        long timestamp = System.currentTimeMillis();
        this.accountNumber = prefix + String.valueOf(timestamp).substring(5);
    }
    
    public Account() {}
    
    public Account(AccountType accountType, Customer customer, Employee createdBy) {
        this.accountType = accountType;
        this.customer = customer;
        this.createdBy = createdBy;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    
    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }
    
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    
    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
    
    public BigDecimal getCreditLimit() { return creditLimit; }
    public void setCreditLimit(BigDecimal creditLimit) { this.creditLimit = creditLimit; }
    
    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    
    public Employee getCreatedBy() { return createdBy; }
    public void setCreatedBy(Employee createdBy) { this.createdBy = createdBy; }
}
