package com.example.EmployeeManagementSystemAdvance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryResponseDTO {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private String department;
    private Double amount;
    private LocalDate effectiveDate;
    private LocalDate endDate;
    private String salaryType;
    private String reason;
    private boolean current;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String message;
}