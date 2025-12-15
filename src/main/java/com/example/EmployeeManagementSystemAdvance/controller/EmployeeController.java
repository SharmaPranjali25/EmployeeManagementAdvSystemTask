//package com.example.EmployeeManagementSystem;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import com.example.EmployeeManagementSystem.dto.EmployeeRequestDTO;
//import com.example.EmployeeManagementSystem.dto.EmployeeResponseDTO;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/employees")
//public class EmployeeController {
//
//    @Autowired
//    private EmployeeService service;
//
//    // Add Employee
//    @PostMapping("/add")
//    public ResponseEntity<EmployeeResponseDTO> addEmployee(@RequestBody EmployeeRequestDTO requestDTO) {
//        Employee employee = new Employee();
//        employee.setName(requestDTO.getName());
//        employee.setDepartment(requestDTO.getDepartment());
//        employee.setSalary(requestDTO.getSalary());
//
//        Employee savedEmployee = service.saveEmployee(employee);
//
//        return new ResponseEntity<>(
//                new EmployeeResponseDTO(
//                        savedEmployee.getId(),
//                        savedEmployee.getName(),
//                        savedEmployee.getDepartment()
//                ),
//                HttpStatus.CREATED
//        );
//    }
//
//    // Get All Employees
//    @GetMapping("/all")
//    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees() {
//        List<EmployeeResponseDTO> employees = service.getAllEmployees().stream()
//                .map(emp -> new EmployeeResponseDTO(
//                        emp.getId(),
//                        emp.getName(),
//                        emp.getDepartment()
//                ))
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(employees);
//    }
//
//    // Get Employee by ID
//    @GetMapping("/{id}")
//    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Long id) {
//        Employee emp = service.getEmployeeById(id)
//                .orElseThrow(() ->
//                        new RuntimeException("Employee not found with id: " + id));
//
//        return ResponseEntity.ok(
//                new EmployeeResponseDTO(
//                        emp.getId(),
//                        emp.getName(),
//                        emp.getDepartment()
//                )
//        );
//    }
//
//    // Update Employee
//    @PutMapping("/update/{id}")
//    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
//            @PathVariable Long id,
//            @RequestBody EmployeeRequestDTO requestDTO) {
//
//        EmployeeRequestDTO updatedEmployee = new EmployeeRequestDTO();
//        updatedEmployee.setName(requestDTO.getName());
//        updatedEmployee.setDepartment(requestDTO.getDepartment());
//        updatedEmployee.setSalary(requestDTO.getSalary());
//
//        EmployeeResponseDTO emp = service.updateEmployee(id, updatedEmployee);
//
//        if (emp == null) {
//            throw new RuntimeException("Employee not found with id: " + id);
//        }
//
//        return ResponseEntity.ok(
//                new EmployeeResponseDTO(
//                        emp.getId(),
//                        emp.getName(),
//                        emp.getDepartment()
//                )
//        );
//    }
//
//    // Delete Employee
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
//        service.deleteEmployee(id);
//        return ResponseEntity.ok("Employee deleted successfully!");
//    }
//}

// ============================================================================
// ========================== WITH LOGGING ====================================
// ============================================================================

package com.example.EmployeeManagementSystemAdvance.controller;

import com.example.EmployeeManagementSystemAdvance.exception.ResourceNotFoundException;
import com.example.EmployeeManagementSystemAdvance.service.EmployeeService;
import com.example.EmployeeManagementSystemAdvance.dto.EmployeeRequestDTO;
import com.example.EmployeeManagementSystemAdvance.dto.EmployeeResponseDTO;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService service;

    @Autowired
    private ModelMapper modelMapper;

    // Add Employee
//    @PostMapping("/add")
//    public ResponseEntity<EmployeeResponseDTO> addEmployee(@RequestBody EmployeeRequestDTO requestDTO) {
//        log.info("POST /employees/add - Request to add employee: {}", requestDTO.getName());
//
//        EmployeeResponseDTO savedEmployee = service.saveEmployee(requestDTO);
//
//        return new ResponseEntity<>(
//                new EmployeeResponseDTO(
//                        savedEmployee.getId(),
//                        savedEmployee.getName(),
//                        savedEmployee.getDepartment()
//                ),
//                HttpStatus.CREATED
//        );
//    }

    // WITH MODELMAPPER
    @PostMapping("/add")
    public ResponseEntity<EmployeeResponseDTO> addEmployee(@RequestBody EmployeeRequestDTO requestDTO) {
        log.info("POST /employees/add - Request to add employee: {}", requestDTO.getName());

        var savedEmployeeEntity = service.saveEmployeeEntity(requestDTO);
        EmployeeResponseDTO savedEmployeeDTO =
                modelMapper.map(savedEmployeeEntity, EmployeeResponseDTO.class);

        log.debug("Employee saved successfully: {}", savedEmployeeDTO.getName());
        return new ResponseEntity<>(savedEmployeeDTO, HttpStatus.CREATED);
    }

    // Get All Employees
//    @GetMapping("/all")
//    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees() {
//        log.info("GET /employees/all - Request to fetch all employees");
//
//        List<EmployeeResponseDTO> employees = service.getAllEmployees().stream()
//                .map(emp -> new EmployeeResponseDTO(
//                        emp.getId(),
//                        emp.getName(),
//                        emp.getDepartment()
//                ))
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(employees);
//    }

    @GetMapping("/all")
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees() {
        log.info("GET /employees/all - Request to fetch all employees");

        List<EmployeeResponseDTO> employees = service.getAllEmployees().stream()
                .map(emp -> modelMapper.map(emp, EmployeeResponseDTO.class))
                .collect(Collectors.toList());

        log.debug("Total employees fetched: {}", employees.size());
        return ResponseEntity.ok(employees);
    }

    // Get Employee by ID
//    @GetMapping("/{id}")
//    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Long id) {
//        log.info("GET /employees/{} - Request to fetch employee by ID", id);
//
//        EmployeeResponseDTO emp = service.getEmployeeById(id)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException("Employee not found with id: " + id));
//
//        return ResponseEntity.ok(
//                new EmployeeResponseDTO(
//                        emp.getId(),
//                        emp.getName(),
//                        emp.getDepartment()
//                )
//        );
//    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Long id) {
        log.info("GET /employees/{} - Request to fetch employee by ID", id);

        var employeeEntity = service.getEmployeeById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id: " + id));

        EmployeeResponseDTO responseDTO =
                modelMapper.map(employeeEntity, EmployeeResponseDTO.class);

        return ResponseEntity.ok(responseDTO);
    }

    // Update Employee
//    @PutMapping("/update/{id}")
//    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
//            @PathVariable Long id,
//            @RequestBody EmployeeRequestDTO requestDTO) {
//
//        log.info("PUT /employees/update/{} - Request to update employee: {}", id, requestDTO.getName());
//
//        EmployeeRequestDTO updatedEmployee = new EmployeeRequestDTO();
//        updatedEmployee.setName(requestDTO.getName());
//        updatedEmployee.setDepartment(requestDTO.getDepartment());
//        updatedEmployee.setSalary(requestDTO.getSalary());
//
//        EmployeeResponseDTO emp = service.updateEmployee(id, updatedEmployee);
//
//        if (emp == null) {
//            throw new ResourceNotFoundException("Employee not found with id: " + id);
//        }
//
//        return ResponseEntity.ok(
//                new EmployeeResponseDTO(
//                        emp.getId(),
//                        emp.getName(),
//                        emp.getDepartment()
//                )
//        );
//    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeRequestDTO requestDTO) {

        log.info("PUT /employees/update/{} - Request to update employee: {}", id, requestDTO.getName());

        var updatedEmployeeEntity = service.updateEmployee(id, requestDTO);
        EmployeeResponseDTO responseDTO =
                modelMapper.map(updatedEmployeeEntity, EmployeeResponseDTO.class);

        log.debug("Employee updated successfully: {}", responseDTO.getName());
        return ResponseEntity.ok(responseDTO);
    }

    // Delete Employee
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
//        log.info("DELETE /employees/delete/{} - Request to delete employee", id);
//
//        service.deleteEmployee(id);
//
//        return ResponseEntity.ok("Employee deleted successfully!");
//    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        log.info("DELETE /employees/delete/{} - Request to delete employee", id);

        service.deleteEmployee(id);

        log.debug("Employee deleted successfully with ID: {}", id);
        return ResponseEntity.ok("Employee deleted successfully!");
    }
}
