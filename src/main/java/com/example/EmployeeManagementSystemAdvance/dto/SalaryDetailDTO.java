package com.example.EmployeeManagementSystemAdvance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryDetailDTO {
    private Long salaryId;
    private Double amount;
    private LocalDate effectiveDate;
    private LocalDate endDate;
    private String salaryType;
    private String reason;
    private boolean current;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}