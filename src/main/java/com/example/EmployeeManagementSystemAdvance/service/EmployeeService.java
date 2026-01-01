package com.example.EmployeeManagementSystemAdvance.service;

import com.example.EmployeeManagementSystemAdvance.dto.EmployeeRequestDTO;
import com.example.EmployeeManagementSystemAdvance.dto.EmployeeResponseDTO;
import com.example.EmployeeManagementSystemAdvance.dto.EmployeeResponseV2DTO;

import java.util.List;

public interface EmployeeService {

    // ===== V1 Methods =====
    EmployeeResponseDTO saveEmployeeDTO(EmployeeRequestDTO dto);
    List<EmployeeResponseDTO> getAllEmployeesDTO();
    EmployeeResponseDTO getEmployeeByIdDTO(Long id);
    EmployeeResponseDTO updateEmployeeDTO(Long id, EmployeeRequestDTO dto);
    void deleteEmployee(Long id);

    // ===== V2 Methods =====
    EmployeeResponseV2DTO saveEmployeeV2(EmployeeRequestDTO dto);
    List<EmployeeResponseV2DTO> getAllEmployeesV2();
    EmployeeResponseV2DTO getEmployeeByIdV2(Long id);
    EmployeeResponseV2DTO updateEmployeeV2(Long id, EmployeeRequestDTO dto);

    // NEW: Search Employee (V2)
    EmployeeResponseV2DTO searchEmployee(Long id, String firstName, String lastName);
}

