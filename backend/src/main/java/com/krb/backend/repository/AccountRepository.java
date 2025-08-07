package com.krb.backend.repository;

import com.krb.backend.entity.Account;
import com.krb.backend.entity.Customer;
import com.krb.backend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    Optional<Account> findByAccountNumber(String accountNumber);
    
    boolean existsByAccountNumber(String accountNumber);
    
    List<Account> findByCustomer(Customer customer);
    
    List<Account> findByCustomerId(Long customerId);
    
    List<Account> findByAccountType(Account.AccountType accountType);
    
    List<Account> findByStatus(Account.AccountStatus status);
    
    List<Account> findByCreatedBy(Employee employee);
    
    @Query("SELECT a FROM Account a WHERE a.customer.id = :customerId ORDER BY a.createdAt DESC")
    List<Account> findByCustomerIdOrderByCreatedAtDesc(@Param("customerId") Long customerId);
    
    @Query("SELECT a FROM Account a WHERE a.createdBy.id = :employeeId ORDER BY a.createdAt DESC")
    List<Account> findByCreatedByEmployeeId(@Param("employeeId") Long employeeId);
    
    @Query("SELECT a FROM Account a WHERE a.balance > :minBalance")
    List<Account> findAccountsWithBalanceGreaterThan(@Param("minBalance") BigDecimal minBalance);
    
    @Query("SELECT a.accountType, COUNT(a) FROM Account a GROUP BY a.accountType")
    List<Object[]> countAccountsByType();
    
    @Query("SELECT a.status, COUNT(a) FROM Account a GROUP BY a.status")
    List<Object[]> countAccountsByStatus();
    
    @Query("SELECT COUNT(a) FROM Account a")
    long countAccounts();
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.customer.id = :customerId")
    long countAccountsByCustomer(@Param("customerId") Long customerId);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.createdBy.id = :employeeId")
    long countAccountsByEmployee(@Param("employeeId") Long employeeId);
    
    @Query("SELECT SUM(a.balance) FROM Account a WHERE a.status = 'ACTIVE'")
    BigDecimal getTotalActiveBalance();
}
