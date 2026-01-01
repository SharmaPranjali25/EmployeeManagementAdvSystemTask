package com.example.EmployeeManagementSystemAdvance.controller;

import com.example.EmployeeManagementSystemAdvance.dto.EmployeeRequestDTO;
import com.example.EmployeeManagementSystemAdvance.dto.EmployeeResponseDTO;
import com.example.EmployeeManagementSystemAdvance.service.EmployeeService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {
	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    @Autowired
    private EmployeeService service;
    @Value("${app.title}")
    private String appTitle;
    
    @Value("${app.description}")
    private String appDescription;

    @PostMapping("/add")
    public ResponseEntity<EmployeeResponseDTO> addEmployee(@RequestBody EmployeeRequestDTO requestDTO) {
    	   logger.info("*************************************************");
           logger.info("Application Title: {}", appTitle);
           logger.info("Application Description: {}", appDescription);
           logger.info("*************************************************");
           logger.info("Received request to add employee: {}", requestDTO);
        EmployeeResponseDTO savedDTO = service.saveEmployeeDTO(requestDTO);
        logger.info("Employee added successfully with ID: {}", savedDTO.getId());
        return new ResponseEntity<>(savedDTO, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees() {
        List<EmployeeResponseDTO> employees = service.getAllEmployeesDTO();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Long id) {
        EmployeeResponseDTO dto = service.getEmployeeByIdDTO(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(@PathVariable Long id,
                                                              @RequestBody EmployeeRequestDTO requestDTO) {
        EmployeeResponseDTO updatedDTO = service.updateEmployeeDTO(id, requestDTO);
        return ResponseEntity.ok(updatedDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        service.deleteEmployee(id);
        return ResponseEntity.ok("Employee deleted successfully!");
    }
}
