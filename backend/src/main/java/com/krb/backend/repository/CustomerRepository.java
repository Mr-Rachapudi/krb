package com.krb.backend.repository;

import com.krb.backend.entity.Customer;
import com.krb.backend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    Optional<Customer> findByEmail(String email);
    
    Optional<Customer> findBySsn(String ssn);
    
    boolean existsByEmail(String email);
    
    boolean existsBySsn(String ssn);
    
    List<Customer> findByCreatedBy(Employee employee);
    
    @Query("SELECT c FROM Customer c WHERE c.createdBy.id = :employeeId ORDER BY c.createdAt DESC")
    List<Customer> findByCreatedByEmployeeId(@Param("employeeId") Long employeeId);
    
    @Query("SELECT c FROM Customer c WHERE " +
           "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Customer> searchCustomers(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT c FROM Customer c JOIN FETCH c.accounts ORDER BY c.createdAt DESC")
    List<Customer> findAllWithAccounts();
    
    @Query("SELECT COUNT(c) FROM Customer c")
    long countCustomers();
    
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.createdBy.id = :employeeId")
    long countCustomersByEmployee(@Param("employeeId") Long employeeId);
}
