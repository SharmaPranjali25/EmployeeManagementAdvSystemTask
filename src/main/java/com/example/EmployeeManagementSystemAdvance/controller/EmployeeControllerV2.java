package com.example.EmployeeManagementSystemAdvance.controller;

import com.example.EmployeeManagementSystemAdvance.dto.EmployeeRequestDTO;
import com.example.EmployeeManagementSystemAdvance.dto.EmployeeResponseV2DTO;
import com.example.EmployeeManagementSystemAdvance.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v2/employees")
public class EmployeeControllerV2 {

    @Autowired
    private EmployeeService service;

    @PostMapping("/add")
    public ResponseEntity<EmployeeResponseV2DTO> addEmployee(@RequestBody EmployeeRequestDTO dto) {
        EmployeeResponseV2DTO response = service.saveEmployeeV2(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<EmployeeResponseV2DTO>> getAllEmployees() {
        List<EmployeeResponseV2DTO> employees = service.getAllEmployeesV2();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseV2DTO> getEmployee(@PathVariable Long id) {
        EmployeeResponseV2DTO employee = service.getEmployeeByIdV2(id);
        return ResponseEntity.ok(employee);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EmployeeResponseV2DTO> updateEmployee(@PathVariable Long id,
                                                                @RequestBody EmployeeRequestDTO dto) {
        EmployeeResponseV2DTO updated = service.updateEmployeeV2(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        service.deleteEmployee(id);
        return ResponseEntity.ok("Employee deleted successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<EmployeeResponseV2DTO> searchEmployee(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName) {
        EmployeeResponseV2DTO result = service.searchEmployee(id, firstName, lastName);
        return ResponseEntity.ok(result);
    }
}