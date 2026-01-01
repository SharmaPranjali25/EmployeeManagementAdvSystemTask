package com.example.EmployeeManagementSystemAdvance.controller;

import com.example.EmployeeManagementSystemAdvance.dto.*;
import com.example.EmployeeManagementSystemAdvance.service.SalaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/salaries")
public class SalaryController {
    
    private static final Logger logger = LoggerFactory.getLogger(SalaryController.class);

    @Autowired
    private SalaryService salaryService;

    // POST /create
    //DONE
    
    @PostMapping("/create")
    public ResponseEntity<SalaryResponseDTO> createSalary(@RequestBody SalaryRequestDTO dto) {
        logger.info("Received request to create salary for employee: {}", dto.getEmployeeId());

        // Service layer handles null amount
        SalaryResponseDTO response = salaryService.createSalary(dto);

        logger.info("Salary created successfully with ID: {}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalaryResponseDTO> getSalaryById(@PathVariable Long id) {
        logger.info("Fetching salary with ID: {}", id);
        SalaryResponseDTO salary = salaryService.getSalaryById(id);
        return ResponseEntity.ok(salary);
    }

    @GetMapping("/all")
    public ResponseEntity<List<SalaryResponseDTO>> getAllSalaries() {
        logger.info("Fetching all salary records");
        List<SalaryResponseDTO> salaries = salaryService.getAllSalaries();
        return ResponseEntity.ok(salaries);
    }

    @GetMapping("/current")
    public ResponseEntity<List<SalaryResponseDTO>> getAllCurrentSalaries() {
        logger.info("Fetching all current salary records");
        List<SalaryResponseDTO> salaries = salaryService.getAllCurrentSalaries();
        return ResponseEntity.ok(salaries);
    }
    
//DONE
    @GetMapping("/employee/{employeeId}/history")
    public ResponseEntity<SalaryHistoryDTO> getEmployeeSalaryHistory(@PathVariable Long employeeId) {
        logger.info("Fetching salary history for employee: {}", employeeId);
        SalaryHistoryDTO history = salaryService.getEmployeeSalaryHistory(employeeId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/employee/{employeeId}/current")
    public ResponseEntity<SalaryResponseDTO> getCurrentSalary(@PathVariable Long employeeId) {
        logger.info("Fetching current salary for employee: {}", employeeId);
        SalaryResponseDTO salary = salaryService.getCurrentSalaryByEmployeeId(employeeId);
        return ResponseEntity.ok(salary);
    }

    @PostMapping("/increment")
    public ResponseEntity<SalaryResponseDTO> processSalaryIncrement(
            @RequestBody SalaryIncrementRequestDTO dto) {
        logger.info("Processing salary increment for employee: {}", dto.getEmployeeId());
        SalaryResponseDTO response = salaryService.processSalaryIncrement(dto);
        logger.info("Salary increment processed successfully");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SalaryResponseDTO> updateSalary(
            @PathVariable Long id, 
            @RequestBody SalaryRequestDTO dto) {
        logger.info("Updating salary with ID: {}", id);
        SalaryResponseDTO updated = salaryService.updateSalary(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<SalaryResponseDTO>> getSalariesByDepartment(
            @PathVariable String department) {
        logger.info("Fetching salaries for department: {}", department);
        List<SalaryResponseDTO> salaries = salaryService.getSalariesByDepartment(department);
        return ResponseEntity.ok(salaries);
    }

    @GetMapping("/statistics/department")
    public ResponseEntity<List<DepartmentSalaryStatsDTO>> getDepartmentStatistics() {
        logger.info("Fetching department-wise salary statistics");
        List<DepartmentSalaryStatsDTO> stats = salaryService.getDepartmentSalaryStatistics();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<SalaryResponseDTO>> getSalariesInDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        logger.info("Fetching salaries between {} and {}", startDate, endDate);
        List<SalaryResponseDTO> salaries = salaryService.getSalariesInDateRange(startDate, endDate);
        return ResponseEntity.ok(salaries);
    }

    @GetMapping("/total-expenditure")
    public ResponseEntity<Double> getTotalSalaryExpenditure() {
        logger.info("Calculating total salary expenditure");
        Double total = salaryService.getTotalSalaryExpenditure();
        return ResponseEntity.ok(total);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSalary(@PathVariable Long id) {
        logger.info("Deleting salary with ID: {}", id);
        salaryService.deleteSalary(id);
        return ResponseEntity.ok("Salary record deleted successfully");
    }
}
