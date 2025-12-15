package com.example.EmployeeManagementSystemAdvance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.EmployeeManagementSystemAdvance.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> { 

} 