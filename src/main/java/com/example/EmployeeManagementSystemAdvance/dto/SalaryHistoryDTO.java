package com.example.EmployeeManagementSystemAdvance.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//===== Salary History DTO =====
@Data
@NoArgsConstructor
@AllArgsConstructor
public
class SalaryHistoryDTO {
 private Long employeeId;
 private String employeeName;
 private String department;
 private double currentSalary;
 private java.util.List<SalaryDetailDTO> salaryHistory;
}
