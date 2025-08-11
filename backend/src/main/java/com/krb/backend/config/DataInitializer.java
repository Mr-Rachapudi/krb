package com.krb.backend.config;

import com.krb.backend.entity.Employee;
import com.krb.backend.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        if (employeeRepository.count() == 0) {
            Employee admin = new Employee();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFirstName("System");
            admin.setLastName("Administrator");
            admin.setEmail("admin@krb.com");
            admin.setRole(Employee.Role.ADMIN);
            employeeRepository.save(admin);
            
            Employee employee = new Employee();
            employee.setUsername("employee1");
            employee.setPassword(passwordEncoder.encode("emp123"));
            employee.setFirstName("John");
            employee.setLastName("Doe");
            employee.setEmail("john.doe@krb.com");
            employee.setRole(Employee.Role.EMPLOYEE);
            employeeRepository.save(employee);
            
            System.out.println("Default users created:");
            System.out.println("Admin - Username: admin, Password: admin123");
            System.out.println("Employee - Username: employee1, Password: emp123");
        }
    }
}
