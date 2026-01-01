package com.example.EmployeeManagementSystemAdvance.dto;

import com.example.EmployeeManagementSystemAdvance.entity.Salary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryRequestDTO {
    private Long employeeId;
    private Double amount;  // Changed from double to Double (wrapper class)
    private LocalDate effectiveDate;
    private Salary.SalaryType salaryType;
    private String reason;
}