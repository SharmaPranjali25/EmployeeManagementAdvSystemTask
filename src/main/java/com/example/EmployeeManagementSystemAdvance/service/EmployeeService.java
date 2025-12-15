//package com.example.EmployeeManagementSystem;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.example.EmployeeManagementSystem.dto.EmployeeRequestDTO;
//import com.example.EmployeeManagementSystem.dto.EmployeeResponseDTO;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class EmployeeService {
//
//    @Autowired
//    private EmployeeRepository repository;
//
//    // Create Employee from Request DTO
//    public EmployeeResponseDTO saveEmployee(EmployeeRequestDTO dto) {
//        Employee employee = new Employee();
//        employee.setName(dto.getName());
//        employee.setDepartment(dto.getDepartment());
//        employee.setSalary(dto.getSalary());
//
//        Employee saved = repository.save(employee);
//
//        return mapToResponseDTO(saved);
//    }
//
//    // Get all Employees as Response DTOs
//    public List<EmployeeResponseDTO> getAllEmployees() {
//        return repository.findAll()
//                .stream()
//                .map(this::mapToResponseDTO)
//                .collect(Collectors.toList());
//    }
//
//    // Get Employee by ID as Response DTO
//    public Optional<EmployeeResponseDTO> getEmployeeById(Long id) {
//        return repository.findById(id)
//                .map(this::mapToResponseDTO);
//    }
//
//    // Update Employee using Request DTO
//    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto) {
//        Optional<Employee> existing = repository.findById(id);
//        if (existing.isPresent()) {
//            Employee emp = existing.get();
//            emp.setName(dto.getName());
//            emp.setDepartment(dto.getDepartment());
//            emp.setSalary(dto.getSalary());
//            Employee updated = repository.save(emp);
//            return mapToResponseDTO(updated);
//        }
//        return null;
//    }
//
//    // Delete Employee
//    public void deleteEmployee(Long id) {
//        repository.deleteById(id);
//    }
//
//    // it basically take Employee entity → EmployeeResponseDTO
//    private EmployeeResponseDTO mapToResponseDTO(Employee employee) {
//        EmployeeResponseDTO response = new EmployeeResponseDTO();
//        response.setId(employee.getId());
//        response.setName(employee.getName());
//        response.setDepartment(employee.getDepartment());
//        // salary is intentionally hidden in response
//        return response;
//    }
//}

//WITH LOGGING
package com.example.EmployeeManagementSystemAdvance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.EmployeeManagementSystemAdvance.dto.EmployeeRequestDTO;
import com.example.EmployeeManagementSystemAdvance.dto.EmployeeResponseDTO;
import com.example.EmployeeManagementSystemAdvance.entity.Employee;
import com.example.EmployeeManagementSystemAdvance.exception.ResourceNotFoundException;
import com.example.EmployeeManagementSystemAdvance.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    // Create Employee
//    public EmployeeResponseDTO saveEmployee(EmployeeRequestDTO dto) {
//        log.info("Request to save employee: {}", dto.getName());
//
//        Employee employee = new Employee();
//        employee.setName(dto.getName());
//        employee.setDepartment(dto.getDepartment());
//        employee.setSalary(dto.getSalary());
//
//        Employee saved = repository.save(employee);
//        log.debug("Employee saved successfully: {}", saved);
//        return mapToResponseDTO(saved);
//    }
// USING MODEL MAPPER
    public Employee saveEmployeeEntity(EmployeeRequestDTO dto) {
        log.info("Request to save employee: {}", dto.getName());

        // DTO → Entity
        Employee employee = modelMapper.map(dto, Employee.class);

        Employee saved = repository.save(employee);
        log.debug("Employee saved successfully: {}", saved);
        return saved;
    }

    public EmployeeResponseDTO saveEmployee(EmployeeRequestDTO dto) {
        Employee saved = saveEmployeeEntity(dto);

        // Entity → DTO
        return modelMapper.map(saved, EmployeeResponseDTO.class);
    }

    
    // Get all Employees
//    public List<EmployeeResponseDTO> getAllEmployees() {
//        log.info("Fetching all employees");
//
//        List<EmployeeResponseDTO> employees = repository.findAll()
//                .stream()
//                .map(this::mapToResponseDTO)
//                .collect(Collectors.toList());
//
//        log.debug("Total employees fetched: {}", employees.size());
//        return employees;
//    }
    
    public List<EmployeeResponseDTO> getAllEmployees() {
        log.info("Fetching all employees");

        List<EmployeeResponseDTO> employees = repository.findAll()
                .stream()
                .map(emp -> modelMapper.map(emp, EmployeeResponseDTO.class))  // map each entity
                .collect(Collectors.toList());

        log.debug("Total employees fetched: {}", employees.size());
        return employees;
    }

    // Get Employee by ID
//    public EmployeeResponseDTO getEmployeeById(Long id) {
//        log.info("Fetching employee by ID: {}", id);
//
//        Employee employee = repository.findById(id)
//                .orElseThrow(() -> {
//                    log.warn("Employee not found with ID: {}", id);
//                    return new ResourceNotFoundException("Employee not found with id: " + id);
//                });
//
//        log.debug("Employee found: {}", employee.getName());
//        return mapToResponseDTO(employee);
//    }
    
    public Optional<Employee> getEmployeeById(Long id) {
        log.info("Fetching employee by ID: {}", id);
        return repository.findById(id);
    }

    

    // Update Employee
//    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto) {
//        log.info("Request to update employee with ID: {}", id);
//
//        Employee employee = repository.findById(id)
//                .orElseThrow(() -> {
//                    log.warn("Employee not found with ID: {}", id);
//                    return new ResourceNotFoundException("Employee not found with id: " + id);
//                });
//
//        employee.setName(dto.getName());
//        employee.setDepartment(dto.getDepartment());
//        employee.setSalary(dto.getSalary());
//
//        Employee updated = repository.save(employee);
//        log.debug("Employee updated successfully: {}", updated);
//        return mapToResponseDTO(updated);
//    }
    
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto) {
        log.info("Request to update employee with ID: {}", id);

        Employee employee = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Employee not found with ID: {}", id);
                    return new ResourceNotFoundException("Employee not found with id: " + id);
                });

        // Map fields from DTO → Entity
        modelMapper.map(dto, employee); // only updates fields present in DTO

        Employee updated = repository.save(employee);
        log.debug("Employee updated successfully: {}", updated);

        return modelMapper.map(updated, EmployeeResponseDTO.class); // map updated entity to DTO
    }
    
    

    // Delete Employee
//    public void deleteEmployee(Long id) {
//        log.info("Request to delete employee with ID: {}", id);
//
//        Employee employee = repository.findById(id)
//                .orElseThrow(() -> {
//                    log.warn("Employee not found with ID: {}", id);
//                    return new ResourceNotFoundException("Employee not found with id: " + id);
//                });
//
//        repository.delete(employee);
//        log.debug("Employee deleted successfully with ID: {}", id);
//    }
    
    public void deleteEmployee(Long id) {
        log.info("Request to delete employee with ID: {}", id);

        Employee employee = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Employee not found with ID: {}", id);
                    return new ResourceNotFoundException("Employee not found with id: " + id);
                });

        repository.delete(employee);
        log.debug("Employee deleted successfully with ID: {}", id);
    }

    // Map Employee entity → EmployeeResponseDTO
//    private EmployeeResponseDTO mapToResponseDTO(Employee employee) {
//        EmployeeResponseDTO response = new EmployeeResponseDTO();
//        response.setId(employee.getId());
//        response.setName(employee.getName());
//        response.setDepartment(employee.getDepartment());
//        return response;
//    }
}
