package com.krb.backend.service;

import com.krb.backend.dto.CreateEmployeeRequest;
import com.krb.backend.dto.EmployeeDto;
import com.krb.backend.entity.Employee;
import com.krb.backend.repository.CustomerRepository;
import com.krb.backend.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<EmployeeDto> getEmployeesByRole(Employee.Role role) {
        return employeeRepository.findByRole(role).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<EmployeeDto> getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .map(this::convertToDto);
    }
    
    public Optional<EmployeeDto> getEmployeeByUsername(String username) {
        return employeeRepository.findByUsername(username)
                .map(this::convertToDto);
    }
    
    public EmployeeDto createEmployee(CreateEmployeeRequest request) {
        if (employeeRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        Employee employee = new Employee();
        employee.setUsername(request.getUsername());
        employee.setPassword(passwordEncoder.encode(request.getPassword()));
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setRole(request.getRole());
        
        Employee savedEmployee = employeeRepository.save(employee);
        return convertToDto(savedEmployee);
    }
    
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        if (!employee.getUsername().equals(employeeDto.getUsername()) && 
            employeeRepository.existsByUsername(employeeDto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        if (!employee.getEmail().equals(employeeDto.getEmail()) && 
            employeeRepository.existsByEmail(employeeDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        employee.setUsername(employeeDto.getUsername());
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setEmail(employeeDto.getEmail());
        employee.setRole(employeeDto.getRole());
        
        Employee savedEmployee = employeeRepository.save(employee);
        return convertToDto(savedEmployee);
    }
    
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        long customerCount = customerRepository.countCustomersByEmployee(id);
        if (customerCount > 0) {
            throw new RuntimeException("Cannot delete employee with existing customers");
        }
        
        employeeRepository.delete(employee);
    }
    
    public boolean validateCredentials(String username, String password) {
        Optional<Employee> employee = employeeRepository.findByUsername(username);
        return employee.isPresent() && 
               passwordEncoder.matches(password, employee.get().getPassword());
    }
    
    public long getEmployeeCount() {
        return employeeRepository.countEmployees();
    }
    
    public long getAdminCount() {
        return employeeRepository.countAdmins();
    }
    
    private EmployeeDto convertToDto(Employee employee) {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(employee.getId());
        dto.setUsername(employee.getUsername());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setRole(employee.getRole());
        dto.setCreatedAt(employee.getCreatedAt());
        dto.setUpdatedAt(employee.getUpdatedAt());
        dto.setCustomerCount(customerRepository.countCustomersByEmployee(employee.getId()));
        return dto;
    }
}
