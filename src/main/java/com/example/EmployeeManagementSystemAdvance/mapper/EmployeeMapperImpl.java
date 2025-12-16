package com.example.EmployeeManagementSystemAdvance.mapper;

import com.example.EmployeeManagementSystemAdvance.dto.EmployeeRequestDTO;
import com.example.EmployeeManagementSystemAdvance.dto.EmployeeResponseDTO;
import com.example.EmployeeManagementSystemAdvance.dto.EmployeeResponseV2DTO;
import com.example.EmployeeManagementSystemAdvance.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapperImpl implements EmployeeMapper {

    @Override
    public Employee toEntity(EmployeeRequestDTO dto) {
        if (dto == null) return null;
        Employee emp = new Employee();
        emp.setFirstName(dto.getFirstName());
        emp.setLastName(dto.getLastName());
        emp.setDepartment(dto.getDepartment());
        emp.setSalary(dto.getSalary());
        return emp;
    }

    @Override
    public EmployeeResponseDTO toResponseDTO(Employee employee) {
        if (employee == null) return null;
        EmployeeResponseDTO dto = new EmployeeResponseDTO();
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setDepartment(employee.getDepartment());
        return dto;
    }

    @Override
    public EmployeeResponseV2DTO toResponseV2DTO(Employee employee) {
        if (employee == null) return null;
        EmployeeResponseV2DTO dto = new EmployeeResponseV2DTO();
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setDepartment(employee.getDepartment());
        dto.setSalary((int) employee.getSalary());
        return dto;
    }
}
