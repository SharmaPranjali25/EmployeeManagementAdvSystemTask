package com.example.EmployeeManagementSystemAdvance.mapper;

import com.example.EmployeeManagementSystemAdvance.dto.*;
import com.example.EmployeeManagementSystemAdvance.entity.Employee;
import com.example.EmployeeManagementSystemAdvance.entity.Salary;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SalaryMapper {

    public Salary toEntity(SalaryRequestDTO dto, Employee employee) {
        if (dto == null) return null;

        Salary salary = new Salary();
        salary.setEmployee(employee);
        // Don't set amount here - it will be set in service layer
        // salary.setAmount(dto.getAmount()); // Remove this line
        salary.setEffectiveDate(dto.getEffectiveDate());
        salary.setSalaryType(dto.getSalaryType());
        salary.setReason(dto.getReason());
        salary.setCurrent(true);

        return salary;
    }

    public SalaryResponseDTO toResponseDTO(Salary salary) {
        if (salary == null) return null;

        SalaryResponseDTO dto = new SalaryResponseDTO();
        dto.setId(salary.getId());
        dto.setEmployeeId(salary.getEmployee().getId());
        dto.setEmployeeName(salary.getEmployee().getFirstName() + " " +
                           salary.getEmployee().getLastName());
        dto.setDepartment(salary.getEmployee().getDepartment());
        dto.setAmount(salary.getAmount());
        dto.setEffectiveDate(salary.getEffectiveDate());
        dto.setEndDate(salary.getEndDate());
        dto.setSalaryType(salary.getSalaryType().name());
        dto.setReason(salary.getReason());
        dto.setCurrent(salary.isCurrent());

        return dto;
    }

    public SalaryDetailDTO toDetailDTO(Salary salary) {
        if (salary == null) return null;

        SalaryDetailDTO dto = new SalaryDetailDTO();
        dto.setSalaryId(salary.getId());
        dto.setAmount(salary.getAmount());
        dto.setEffectiveDate(salary.getEffectiveDate());
        dto.setEndDate(salary.getEndDate());
        dto.setSalaryType(salary.getSalaryType().name());
        dto.setReason(salary.getReason());
        dto.setCurrent(salary.isCurrent());

        return dto;
    }

    public SalaryHistoryDTO toHistoryDTO(Employee employee, List<Salary> salaries) {
        if (employee == null) return null;

        SalaryHistoryDTO dto = new SalaryHistoryDTO();
        dto.setEmployeeId(employee.getId());
        dto.setEmployeeName(employee.getFirstName() + " " + employee.getLastName());
        dto.setDepartment(employee.getDepartment());
        dto.setCurrentSalary(employee.getSalary());

        if (salaries != null) {
            dto.setSalaryHistory(salaries.stream()
                .map(this::toDetailDTO)
                .collect(Collectors.toList()));
        }

        return dto;
    }
}