package com.example.EmployeeManagementSystemAdvance.service;

import com.example.EmployeeManagementSystemAdvance.dto.EmployeeRequestDTO;
import com.example.EmployeeManagementSystemAdvance.dto.EmployeeResponseDTO;
import com.example.EmployeeManagementSystemAdvance.dto.EmployeeResponseV2DTO;
import com.example.EmployeeManagementSystemAdvance.entity.Employee;
import com.example.EmployeeManagementSystemAdvance.exception.DuplicateResourceException;
import com.example.EmployeeManagementSystemAdvance.exception.ResourceNotFoundException;
import com.example.EmployeeManagementSystemAdvance.mapper.EmployeeMapper;
import com.example.EmployeeManagementSystemAdvance.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    // ===== V1 METHODS =====
    @Override
    public EmployeeResponseDTO saveEmployeeDTO(EmployeeRequestDTO dto) {
        if (repository.existsByFirstAndLastNameAndDepartment(
                dto.getFirstName(), dto.getLastName(), dto.getDepartment())) {
            throw new DuplicateResourceException("Duplicate data alert cannot save");
        }
        Employee saved = repository.save(employeeMapper.toEntity(dto));
        return employeeMapper.toResponseDTO(saved);
    }

    @Override
    public List<EmployeeResponseDTO> getAllEmployeesDTO() {
        try {
            List<Employee> employees = repository.findAll();
            if (employees.isEmpty()) {
                throw new ResourceNotFoundException("No employee exist");
            }
            return employees.stream()
                    .map(employeeMapper::toResponseDTO)
                    .collect(Collectors.toList());
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unable to connect to database: " + e.getMessage());
        }
    }

    @Override
    public EmployeeResponseDTO getEmployeeByIdDTO(Long id) {
        Employee emp = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return employeeMapper.toResponseDTO(emp);
    }

    @Override
    public EmployeeResponseDTO updateEmployeeDTO(Long id, EmployeeRequestDTO dto) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        if (repository.existsByFirstAndLastNameAndDepartmentExcludingId(
                id, dto.getFirstName(), dto.getLastName(), dto.getDepartment())) {
            throw new DuplicateResourceException("Duplicate data alert cannot save");
        }

        modelMapper.map(dto, employee);
        Employee updated = repository.save(employee);
        return employeeMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        repository.delete(employee);
    }

    // ===== V2 METHODS =====
    @Override
    public EmployeeResponseV2DTO saveEmployeeV2(EmployeeRequestDTO dto) {
        try {
            if (repository.existsByFirstAndLastNameAndDepartment(
                    dto.getFirstName(), dto.getLastName(), dto.getDepartment())) {
                throw new DuplicateResourceException("Duplicate data alert cannot save");
            }

            Employee employee = employeeMapper.toEntity(dto);
            Employee saved = repository.save(employee);

            EmployeeResponseV2DTO response = employeeMapper.toResponseV2DTO(saved);
            response.setMessage("Employee added successfully");
            return response;

        } catch (DuplicateResourceException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save employee: " + e.getMessage());
        }
    }

    @Override
    public List<EmployeeResponseV2DTO> getAllEmployeesV2() {
        try {
            List<Employee> employees = repository.findAll();
            if (employees.isEmpty()) {
                throw new ResourceNotFoundException("No employee exist");
            }
            return employees.stream()
                    .map(employeeMapper::toResponseV2DTO)
                    .collect(Collectors.toList());
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unable to connect to database: " + e.getMessage());
        }
    }

    @Override
    public EmployeeResponseV2DTO getEmployeeByIdV2(Long id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return employeeMapper.toResponseV2DTO(employee);
    }

    @Override
    public EmployeeResponseV2DTO updateEmployeeV2(Long id, EmployeeRequestDTO dto) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        if (repository.existsByFirstAndLastNameAndDepartmentExcludingId(
                id, dto.getFirstName(), dto.getLastName(), dto.getDepartment())) {
            throw new DuplicateResourceException("Duplicate data alert cannot save");
        }

        modelMapper.map(dto, employee);
        Employee updated = repository.save(employee);

        EmployeeResponseV2DTO response = employeeMapper.toResponseV2DTO(updated);
        response.setMessage("Employee updated successfully");
        return response;
    }

//    @Override
//    public EmployeeResponseV2DTO searchEmployee(Long id, String firstName, String lastName) {
//        List<Employee> employees = repository.searchEmployeeExact(id, firstName, lastName);
//
//        if (employees.isEmpty()) {
//            throw new ResourceNotFoundException("No employee found with given criteria");
//        }
//
//        if (employees.size() > 1) {
//            throw new IllegalArgumentException("Multiple employees found. Please refine your search criteria.");
//        }
//
//        return employeeMapper.toResponseV2DTO(employees.get(0));
//    }
    @Override
    public EmployeeResponseV2DTO searchEmployee(Long id, String firstName, String lastName) {

        if (id == null && firstName == null && lastName == null) {
            throw new IllegalArgumentException("Provide atleast one search criteria");
        }

        List<Employee> employees = repository.searchEmployeeExact(id, firstName, lastName);

        if (employees.isEmpty()) {
            throw new ResourceNotFoundException("No employee found with given criteria");
        }

        if (employees.size() > 1) {
            throw new IllegalArgumentException(
                    "Multiple employees found. Please refine your search criteria.");
        }

        return employeeMapper.toResponseV2DTO(employees.get(0));
    }

}