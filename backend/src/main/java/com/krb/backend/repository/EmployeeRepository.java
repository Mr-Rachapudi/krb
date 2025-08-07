package com.krb.backend.repository;

import com.krb.backend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    Optional<Employee> findByUsername(String username);
    
    Optional<Employee> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    List<Employee> findByRole(Employee.Role role);
    
    @Query("SELECT e FROM Employee e WHERE e.role = 'EMPLOYEE' ORDER BY e.createdAt DESC")
    List<Employee> findAllEmployees();
    
    @Query("SELECT e FROM Employee e WHERE e.role = 'ADMIN' ORDER BY e.createdAt DESC")
    List<Employee> findAllAdmins();
    
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.role = 'EMPLOYEE'")
    long countEmployees();
    
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.role = 'ADMIN'")
    long countAdmins();
}
