package com.example.EmployeeManagementSystemAdvance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
//===== Salary Increment Request DTO =====
@Data
@NoArgsConstructor
@AllArgsConstructor
public
class SalaryIncrementRequestDTO {
 private Long employeeId;
 private Double incrementAmount;
 private Double incrementPercentage;
 private LocalDate effectiveDate;
 private String reason;
}