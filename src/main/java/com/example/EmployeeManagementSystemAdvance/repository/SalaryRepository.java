package com.example.EmployeeManagementSystemAdvance.repository;

import com.example.EmployeeManagementSystemAdvance.entity.Employee;
import com.example.EmployeeManagementSystemAdvance.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SalaryRepository extends JpaRepository<Salary, Long> {

    
    @Query("SELECT s FROM Salary s JOIN FETCH s.employee WHERE s.id = :id")
    Optional<Salary> findByIdWithEmployee(@Param("id") Long id);

    @Query("SELECT s FROM Salary s JOIN FETCH s.employee")
    List<Salary> findAllWithEmployee();

    @Query("SELECT s FROM Salary s JOIN FETCH s.employee WHERE s.employee.id = :employeeId ORDER BY s.effectiveDate DESC")
    List<Salary> findByEmployeeId(@Param("employeeId") Long employeeId);

    
    @Query("SELECT s FROM Salary s JOIN FETCH s.employee WHERE s.employee.id = :employeeId AND s.current = true")
    Optional<Salary> findCurrentSalaryByEmployeeIdWithEmployee(@Param("employeeId") Long employeeId);

    // Keep original without JOIN FETCH (used in some places)
    @Query("SELECT s FROM Salary s WHERE s.employee.id = :employeeId AND s.current = true")
    Optional<Salary> findCurrentSalaryByEmployeeId(@Param("employeeId") Long employeeId);

    // ✅ UPDATED: Find salaries by department with JOIN FETCH
    @Query("SELECT s FROM Salary s JOIN FETCH s.employee WHERE s.employee.department = :department AND s.current = true")
    List<Salary> findCurrentSalariesByDepartmentWithEmployee(@Param("department") String department);

    // Keep original without JOIN FETCH
    @Query("SELECT s FROM Salary s WHERE s.employee.department = :department AND s.current = true")
    List<Salary> findCurrentSalariesByDepartment(@Param("department") String department);

    // ✅ UPDATED: Find all current salaries with JOIN FETCH
    @Query("SELECT s FROM Salary s JOIN FETCH s.employee WHERE s.current = true")
    List<Salary> findAllCurrentSalariesWithEmployee();

    // Keep original without JOIN FETCH
    @Query("SELECT s FROM Salary s WHERE s.current = true")
    List<Salary> findAllCurrentSalaries();

    // ✅ NEW: Find by employee and current with JOIN FETCH
    @Query("SELECT s FROM Salary s JOIN FETCH s.employee e WHERE e = :employee AND s.current = :current")
    List<Salary> findByEmployeeAndCurrentWithEmployee(@Param("employee") Employee employee, @Param("current") boolean current);

    // Keep original (used in transactional methods)
    List<Salary> findByEmployeeAndCurrent(Employee employee, boolean current);

    // ✅ NEW: Find by employee ordered by effective date with JOIN FETCH
    @Query("SELECT s FROM Salary s JOIN FETCH s.employee e WHERE e = :employee ORDER BY s.effectiveDate DESC")
    List<Salary> findByEmployeeOrderByEffectiveDateDescWithEmployee(@Param("employee") Employee employee);

    // Keep original (used in transactional methods)
    List<Salary> findByEmployeeOrderByEffectiveDateDesc(Employee employee);

    // ✅ UNCHANGED: Get salary statistics by department (aggregation doesn't need JOIN FETCH)
    @Query("SELECT s.employee.department as department, " +
           "COUNT(DISTINCT s.employee.id) as employeeCount, " +
           "AVG(s.amount) as averageSalary, " +
           "MIN(s.amount) as minSalary, " +
           "MAX(s.amount) as maxSalary, " +
           "SUM(s.amount) as totalSalary " +
           "FROM Salary s WHERE s.current = true " +
           "GROUP BY s.employee.department")
    List<Object[]> getDepartmentSalaryStatistics();

    // ✅ UPDATED: Find salaries within a date range with JOIN FETCH
    @Query("SELECT s FROM Salary s JOIN FETCH s.employee WHERE s.effectiveDate BETWEEN :startDate AND :endDate")
    List<Salary> findSalariesInDateRangeWithEmployee(@Param("startDate") LocalDate startDate,
                                                       @Param("endDate") LocalDate endDate);

    // Keep original without JOIN FETCH
    @Query("SELECT s FROM Salary s WHERE s.effectiveDate BETWEEN :startDate AND :endDate")
    List<Salary> findSalariesInDateRange(@Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);

    // ✅ UNCHANGED: Find salaries by type
    @Query("SELECT s FROM Salary s WHERE s.salaryType = :type AND s.current = true")
    List<Salary> findBySalaryType(@Param("type") Salary.SalaryType type);

    // ✅ UNCHANGED: Check if employee has active salary
    @Query("SELECT COUNT(s) > 0 FROM Salary s WHERE s.employee.id = :employeeId AND s.current = true")
    boolean hasActiveSalary(@Param("employeeId") Long employeeId);

    // ✅ UNCHANGED: Get total salary expenditure (aggregation doesn't need JOIN FETCH)
    @Query("SELECT SUM(s.amount) FROM Salary s WHERE s.current = true")
    Double getTotalSalaryExpenditure();
}