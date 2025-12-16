package com.example.EmployeeManagementSystemAdvance.repository;

import com.example.EmployeeManagementSystemAdvance.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // ===== Duplicate check for V1 & V2 save =====
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Employee e " +
           "WHERE LOWER(e.firstName) = LOWER(:firstName) " +
           "AND LOWER(e.lastName) = LOWER(:lastName) " +
           "AND LOWER(e.department) = LOWER(:department)")
    boolean existsByFirstAndLastNameAndDepartment(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("department") String department
    );

    // ===== Duplicate check for V1 & V2 update (excluding current ID) =====
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Employee e " +
           "WHERE e.id != :id " +
           "AND LOWER(e.firstName) = LOWER(:firstName) " +
           "AND LOWER(e.lastName) = LOWER(:lastName) " +
           "AND LOWER(e.department) = LOWER(:department)")
    boolean existsByFirstAndLastNameAndDepartmentExcludingId(
            @Param("id") Long id,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("department") String department
    );

    // ===== Search Employee for V2 (single employee expected) =====
    @Query("SELECT e FROM Employee e " +
           "WHERE (:id IS NULL OR e.id = :id) " +
           "AND (:firstName IS NULL OR LOWER(e.firstName) = LOWER(:firstName)) " +
           "AND (:lastName IS NULL OR LOWER(e.lastName) = LOWER(:lastName))")
    List<Employee> searchEmployeeExact(
            @Param("id") Long id,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName
    );

    // Optional: Find all employees (repository already provides findAll())
    List<Employee> findAll();
}
