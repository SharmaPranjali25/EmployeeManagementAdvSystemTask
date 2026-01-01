package com.example.EmployeeManagementSystemAdvance.controller;

import com.example.EmployeeManagementSystemAdvance.dto.EmployeeRequestDTO;
import com.example.EmployeeManagementSystemAdvance.dto.EmployeeResponseV2DTO;
import com.example.EmployeeManagementSystemAdvance.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v2/employees")
public class EmployeeControllerV2 {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeControllerV2.class);
    
    @Autowired
    private EmployeeService service;
    
    @Value("${app.title}")
    private String appTitle;

    @Value("${app.description}")
    private String appDescription;

    @PostMapping("/add")
    public ResponseEntity<EmployeeResponseV2DTO> addEmployee(@RequestBody EmployeeRequestDTO dto) {
        logger.info("*************************************************");
        logger.info("Application Title: {}", appTitle);
        logger.info("Application Description: {}", appDescription);
        logger.info("*************************************************");
        logger.info("Received request to add employee: {}", dto);
        
        EmployeeResponseV2DTO response = service.saveEmployeeV2(dto);
        logger.info("Employee added successfully with ID: {}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<EmployeeResponseV2DTO>> getAllEmployees() {
        logger.info("Fetching all employees");
        List<EmployeeResponseV2DTO> employees = service.getAllEmployeesV2();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseV2DTO> getEmployee(@PathVariable Long id) {
        logger.info("Fetching employee with ID: {}", id);
        EmployeeResponseV2DTO employee = service.getEmployeeByIdV2(id);
        return ResponseEntity.ok(employee);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EmployeeResponseV2DTO> updateEmployee(@PathVariable Long id,
                                                                @RequestBody EmployeeRequestDTO dto) {
        logger.info("Updating employee with ID: {}", id);
        EmployeeResponseV2DTO updated = service.updateEmployeeV2(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        logger.info("Deleting employee with ID: {}", id);
        service.deleteEmployee(id);
        return ResponseEntity.ok("Employee deleted successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<EmployeeResponseV2DTO> searchEmployee(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName) {
        logger.info("Searching employee with criteria - ID: {}, FirstName: {}, LastName: {}", 
                    id, firstName, lastName);
        EmployeeResponseV2DTO result = service.searchEmployee(id, firstName, lastName);
        return ResponseEntity.ok(result);
    }
}