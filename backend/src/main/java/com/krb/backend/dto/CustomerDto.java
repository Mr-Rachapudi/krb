package com.krb.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CustomerDto {
    
    private Long id;
    
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
    
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Phone number should be valid")
    private String phoneNumber;
    
    @NotBlank(message = "Address is required")
    private String address;
    
    private LocalDate dateOfBirth;
    
    @NotBlank(message = "SSN is required")
    @Pattern(regexp = "^[0-9]{3}-[0-9]{2}-[0-9]{4}$", message = "SSN should be in format XXX-XX-XXXX")
    private String ssn;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdByEmployeeId;
    private String createdByEmployeeName;
    private List<AccountDto> accounts;
    private Long accountCount;
    
    public CustomerDto() {}
    
    public CustomerDto(Long id, String firstName, String lastName, String email, 
                      String phoneNumber, String address, LocalDate dateOfBirth, 
                      String ssn, LocalDateTime createdAt, LocalDateTime updatedAt,
                      Long createdByEmployeeId, String createdByEmployeeName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.ssn = ssn;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdByEmployeeId = createdByEmployeeId;
        this.createdByEmployeeName = createdByEmployeeName;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public String getSsn() { return ssn; }
    public void setSsn(String ssn) { this.ssn = ssn; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public Long getCreatedByEmployeeId() { return createdByEmployeeId; }
    public void setCreatedByEmployeeId(Long createdByEmployeeId) { this.createdByEmployeeId = createdByEmployeeId; }
    
    public String getCreatedByEmployeeName() { return createdByEmployeeName; }
    public void setCreatedByEmployeeName(String createdByEmployeeName) { this.createdByEmployeeName = createdByEmployeeName; }
    
    public List<AccountDto> getAccounts() { return accounts; }
    public void setAccounts(List<AccountDto> accounts) { this.accounts = accounts; }
    
    public Long getAccountCount() { return accountCount; }
    public void setAccountCount(Long accountCount) { this.accountCount = accountCount; }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
