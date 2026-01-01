package com.example.EmployeeManagementSystemAdvance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//===== Department Salary Stats DTO =====
@Data
@NoArgsConstructor
@AllArgsConstructor
public
class DepartmentSalaryStatsDTO {
 private String department;
 private long employeeCount;
 private double averageSalary;
 private double minSalary;
 private double maxSalary;
 private double totalSalary;
}