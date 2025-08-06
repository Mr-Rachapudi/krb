package com.krb.backend.service;

import com.krb.backend.dto.AccountDto;
import com.krb.backend.dto.CustomerDto;
import com.krb.backend.entity.Customer;
import com.krb.backend.entity.Employee;
import com.krb.backend.repository.AccountRepository;
import com.krb.backend.repository.CustomerRepository;
import com.krb.backend.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<CustomerDto> getAllCustomersWithAccounts() {
        return customerRepository.findAllWithAccounts().stream()
                .map(this::convertToDtoWithAccounts)
                .collect(Collectors.toList());
    }
    
    public Optional<CustomerDto> getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(this::convertToDto);
    }
    
    public List<CustomerDto> getCustomersByEmployee(Long employeeId) {
        return customerRepository.findByCreatedByEmployeeId(employeeId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<CustomerDto> searchCustomers(String searchTerm) {
        return customerRepository.searchCustomers(searchTerm).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public CustomerDto createCustomer(CustomerDto customerDto, Long employeeId) {
        if (customerRepository.existsByEmail(customerDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        if (customerRepository.existsBySsn(customerDto.getSsn())) {
            throw new RuntimeException("SSN already exists");
        }
        
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        Customer customer = new Customer();
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setEmail(customerDto.getEmail());
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        customer.setAddress(customerDto.getAddress());
        customer.setDateOfBirth(customerDto.getDateOfBirth());
        customer.setSsn(customerDto.getSsn());
        customer.setCreatedBy(employee);
        
        Customer savedCustomer = customerRepository.save(customer);
        return convertToDto(savedCustomer);
    }
    
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        if (!customer.getEmail().equals(customerDto.getEmail()) && 
            customerRepository.existsByEmail(customerDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        if (!customer.getSsn().equals(customerDto.getSsn()) && 
            customerRepository.existsBySsn(customerDto.getSsn())) {
            throw new RuntimeException("SSN already exists");
        }
        
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setEmail(customerDto.getEmail());
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        customer.setAddress(customerDto.getAddress());
        customer.setDateOfBirth(customerDto.getDateOfBirth());
        customer.setSsn(customerDto.getSsn());
        
        Customer savedCustomer = customerRepository.save(customer);
        return convertToDto(savedCustomer);
    }
    
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        long accountCount = accountRepository.countAccountsByCustomer(id);
        if (accountCount > 0) {
            throw new RuntimeException("Cannot delete customer with existing accounts");
        }
        
        customerRepository.delete(customer);
    }
    
    public long getCustomerCount() {
        return customerRepository.countCustomers();
    }
    
    private CustomerDto convertToDto(Customer customer) {
        CustomerDto dto = new CustomerDto();
        dto.setId(customer.getId());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setEmail(customer.getEmail());
        dto.setPhoneNumber(customer.getPhoneNumber());
        dto.setAddress(customer.getAddress());
        dto.setDateOfBirth(customer.getDateOfBirth());
        dto.setSsn(customer.getSsn());
        dto.setCreatedAt(customer.getCreatedAt());
        dto.setUpdatedAt(customer.getUpdatedAt());
        
        if (customer.getCreatedBy() != null) {
            dto.setCreatedByEmployeeId(customer.getCreatedBy().getId());
            dto.setCreatedByEmployeeName(customer.getCreatedBy().getFullName());
        }
        
        dto.setAccountCount(accountRepository.countAccountsByCustomer(customer.getId()));
        return dto;
    }
    
    private CustomerDto convertToDtoWithAccounts(Customer customer) {
        CustomerDto dto = convertToDto(customer);
        
        if (customer.getAccounts() != null) {
            List<AccountDto> accountDtos = customer.getAccounts().stream()
                    .map(this::convertAccountToDto)
                    .collect(Collectors.toList());
            dto.setAccounts(accountDtos);
        }
        
        return dto;
    }
    
    private AccountDto convertAccountToDto(com.krb.backend.entity.Account account) {
        AccountDto dto = new AccountDto();
        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setAccountType(account.getAccountType());
        dto.setBalance(account.getBalance());
        dto.setStatus(account.getStatus());
        dto.setInterestRate(account.getInterestRate());
        dto.setCreditLimit(account.getCreditLimit());
        dto.setCreatedAt(account.getCreatedAt());
        dto.setUpdatedAt(account.getUpdatedAt());
        
        if (account.getCustomer() != null) {
            dto.setCustomerId(account.getCustomer().getId());
            dto.setCustomerName(account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName());
        }
        
        if (account.getCreatedBy() != null) {
            dto.setCreatedByEmployeeId(account.getCreatedBy().getId());
            dto.setCreatedByEmployeeName(account.getCreatedBy().getFullName());
        }
        
        return dto;
    }
}
