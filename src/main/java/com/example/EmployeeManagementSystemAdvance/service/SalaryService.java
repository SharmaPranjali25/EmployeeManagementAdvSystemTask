package com.example.EmployeeManagementSystemAdvance.service;

import com.example.EmployeeManagementSystemAdvance.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface SalaryService {

    // Create / Update
    SalaryResponseDTO createSalary(SalaryRequestDTO requestDTO);

    SalaryResponseDTO updateSalary(Long salaryId, SalaryRequestDTO requestDTO);

    // Read
    SalaryResponseDTO getSalaryById(Long salaryId);

    List<SalaryResponseDTO> getAllSalaries();

    List<SalaryResponseDTO> getAllCurrentSalaries();

    SalaryResponseDTO getCurrentSalaryByEmployeeId(Long employeeId);

    SalaryHistoryDTO getEmployeeSalaryHistory(Long employeeId);

    List<SalaryResponseDTO> getSalariesByDepartment(String department);

    List<SalaryResponseDTO> getSalariesInDateRange(LocalDate startDate, LocalDate endDate);

    List<DepartmentSalaryStatsDTO> getDepartmentSalaryStatistics();

    Double getTotalSalaryExpenditure();

    // Increment
    SalaryResponseDTO processSalaryIncrement(SalaryIncrementRequestDTO dto);

    // Delete
    void deleteSalary(Long salaryId);
}
