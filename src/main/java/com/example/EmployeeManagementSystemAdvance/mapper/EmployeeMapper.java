package com.example.EmployeeManagementSystemAdvance.mapper;

import com.example.EmployeeManagementSystemAdvance.dto.EmployeeRequestDTO;
import com.example.EmployeeManagementSystemAdvance.dto.EmployeeResponseDTO;
import com.example.EmployeeManagementSystemAdvance.dto.EmployeeResponseV2DTO;
import com.example.EmployeeManagementSystemAdvance.entity.Employee;

public interface EmployeeMapper {

    // Convert request DTO to entity
    Employee toEntity(EmployeeRequestDTO dto);

    // V1 mapping: entity to response DTO
    EmployeeResponseDTO toResponseDTO(Employee employee);

    // V2 mapping: entity to V2 response DTO
    EmployeeResponseV2DTO toResponseV2DTO(Employee employee);
}
