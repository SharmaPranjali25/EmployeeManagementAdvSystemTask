package com.example.EmployeeManagementSystemAdvance.service;

import com.example.EmployeeManagementSystemAdvance.dto.*;
import com.example.EmployeeManagementSystemAdvance.entity.Employee;
import com.example.EmployeeManagementSystemAdvance.entity.Salary;
import com.example.EmployeeManagementSystemAdvance.exception.ResourceNotFoundException;
import com.example.EmployeeManagementSystemAdvance.repository.EmployeeRepository;
import com.example.EmployeeManagementSystemAdvance.repository.SalaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)  // ✅ ADDED: Default read-only transactions for all methods
public class SalaryServiceImpl implements SalaryService {

    private final SalaryRepository salaryRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional  // ✅ Write transaction for create
    public SalaryResponseDTO createSalary(SalaryRequestDTO requestDTO) {
        Employee employee = employeeRepository.findById(requestDTO.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id: " + requestDTO.getEmployeeId()));

        Double salaryAmount = requestDTO.getAmount();
        if (salaryAmount == null) {
            salaryAmount = employee.getSalary();
            if (salaryAmount == null) {
                throw new IllegalArgumentException("No valid salary amount provided");
            }
        }

        List<Salary> previous = salaryRepository.findByEmployeeAndCurrent(employee, true);
        for (Salary p : previous) {
            p.setCurrent(false);
            p.setEndDate(requestDTO.getEffectiveDate().minusDays(1));
            salaryRepository.save(p);
        }

        Salary salary = new Salary();
        salary.setEmployee(employee);
        salary.setAmount(salaryAmount);
        salary.setEffectiveDate(requestDTO.getEffectiveDate());
        salary.setSalaryType(requestDTO.getSalaryType());
        salary.setReason(requestDTO.getReason());
        salary.setCurrent(true);

        employee.setSalary(salaryAmount);
        employeeRepository.save(employee);

        return mapToResponseDTO(
                salaryRepository.save(salary),
                "Salary record created successfully"
        );
    }

    @Override
    @Transactional  // ✅ Write transaction for update
    public SalaryResponseDTO updateSalary(Long salaryId, SalaryRequestDTO requestDTO) {
        Salary salary = salaryRepository.findById(salaryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Salary record not found with id: " + salaryId));

        Employee employee = salary.getEmployee();

        Double amount = requestDTO.getAmount();
        if (amount == null) {
            amount = employee.getSalary();
            if (amount == null) {
                throw new IllegalArgumentException("No valid salary amount provided");
            }
        }

        salary.setAmount(amount);
        salary.setEffectiveDate(requestDTO.getEffectiveDate());
        salary.setSalaryType(requestDTO.getSalaryType());
        salary.setReason(requestDTO.getReason());

        if (salary.isCurrent()) {
            employee.setSalary(amount);
            employeeRepository.save(employee);
        }

        return mapToResponseDTO(
                salaryRepository.save(salary),
                "Salary record updated successfully"
        );
    }

    @Override
    public SalaryResponseDTO getSalaryById(Long salaryId) {
        Salary salary = salaryRepository.findByIdWithEmployee(salaryId)  // ✅ CHANGED: Use JOIN FETCH query
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Salary record not found with id: " + salaryId));
        return mapToResponseDTO(salary, "Salary record retrieved successfully");
    }

    @Override
    public List<SalaryResponseDTO> getAllSalaries() {
        List<Salary> salaries = salaryRepository.findAllWithEmployee();  // ✅ CHANGED: Use JOIN FETCH query
        if (salaries.isEmpty()) {
            throw new ResourceNotFoundException("No salary records found");
        }
        return salaries.stream()
                .map(s -> mapToResponseDTO(s, null))
                .collect(Collectors.toList());
    }

    @Override
    public List<SalaryResponseDTO> getAllCurrentSalaries() {
        return salaryRepository.findAllCurrentSalariesWithEmployee().stream()  // ✅ CHANGED: Use JOIN FETCH query
                .map(s -> mapToResponseDTO(s, null))
                .collect(Collectors.toList());
    }

    @Override
    public SalaryResponseDTO getCurrentSalaryByEmployeeId(Long employeeId) {
        Salary salary = salaryRepository.findCurrentSalaryByEmployeeIdWithEmployee(employeeId)  // ✅ CHANGED: Use JOIN FETCH query
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No current salary found for employee id: " + employeeId));
        return mapToResponseDTO(salary, null);
    }

    @Override
    public SalaryHistoryDTO getEmployeeSalaryHistory(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id: " + employeeId));

        List<Salary> salaries =
                salaryRepository.findByEmployeeOrderByEffectiveDateDescWithEmployee(employee);  // ✅ CHANGED: Use JOIN FETCH query

        if (salaries.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No salary history found for employee id: " + employeeId);
        }

        List<SalaryDetailDTO> details = salaries.stream()
                .map(this::mapToSalaryDetailDTO)
                .collect(Collectors.toList());

        SalaryHistoryDTO dto = new SalaryHistoryDTO();
        dto.setEmployeeId(employee.getId());
        dto.setEmployeeName(employee.getFirstName() + " " + employee.getLastName());
        dto.setDepartment(employee.getDepartment());
        dto.setCurrentSalary(employee.getSalary() != null ? employee.getSalary() : 0.0);
        dto.setSalaryHistory(details);
        return dto;
    }

    @Override
    public List<SalaryResponseDTO> getSalariesByDepartment(String department) {
        return salaryRepository.findCurrentSalariesByDepartmentWithEmployee(department).stream()  // ✅ CHANGED: Use JOIN FETCH query
                .map(s -> mapToResponseDTO(s, null))
                .collect(Collectors.toList());
    }

    @Override
    public List<SalaryResponseDTO> getSalariesInDateRange(LocalDate startDate, LocalDate endDate) {
        return salaryRepository.findSalariesInDateRangeWithEmployee(startDate, endDate).stream()  // ✅ CHANGED: Use JOIN FETCH query
                .map(s -> mapToResponseDTO(s, null))
                .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentSalaryStatsDTO> getDepartmentSalaryStatistics() {
        return salaryRepository.getDepartmentSalaryStatistics().stream().map(row -> {
            DepartmentSalaryStatsDTO dto = new DepartmentSalaryStatsDTO();
            dto.setDepartment((String) row[0]);
            dto.setEmployeeCount((Long) row[1]);
            dto.setAverageSalary((Double) row[2]);
            dto.setMinSalary((Double) row[3]);
            dto.setMaxSalary((Double) row[4]);
            dto.setTotalSalary((Double) row[5]);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public Double getTotalSalaryExpenditure() {
        Double total = salaryRepository.getTotalSalaryExpenditure();
        return total != null ? total : 0.0;
    }

    @Override
    @Transactional  // ✅ Write transaction for increment
    public SalaryResponseDTO processSalaryIncrement(SalaryIncrementRequestDTO dto) {
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id: " + dto.getEmployeeId()));

        Double currentSalary = employee.getSalary();
        if (currentSalary == null) {
            throw new IllegalArgumentException("Employee has no existing salary");
        }

        Double increment;

        if (dto.getIncrementAmount() != null) {
            increment = dto.getIncrementAmount();
        } else if (dto.getIncrementPercentage() != null) {
            increment = currentSalary * (dto.getIncrementPercentage() / 100.0);
        } else {
            throw new IllegalArgumentException("Increment amount or percentage required");
        }

        SalaryRequestDTO req = new SalaryRequestDTO();
        req.setEmployeeId(employee.getId());
        req.setAmount(currentSalary + increment);
        req.setEffectiveDate(dto.getEffectiveDate());
        req.setSalaryType(Salary.SalaryType.INCREMENT);
        req.setReason(dto.getReason());

        return createSalary(req);
    }

    @Override
    @Transactional  // ✅ Write transaction for delete
    public void deleteSalary(Long salaryId) {
        Salary salary = salaryRepository.findById(salaryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Salary record not found with id: " + salaryId));

        if (salary.isCurrent()) {
            Employee employee = salary.getEmployee();
            List<Salary> prev =
                    salaryRepository.findByEmployeeOrderByEffectiveDateDesc(employee);

            for (Salary p : prev) {
                if (!p.getId().equals(salaryId)) {
                    p.setCurrent(true);
                    p.setEndDate(null);
                    employee.setSalary(p.getAmount());
                    salaryRepository.save(p);
                    employeeRepository.save(employee);
                    break;
                }
            }
        }
        salaryRepository.delete(salary);
    }

    private SalaryResponseDTO mapToResponseDTO(Salary salary, String message) {
        Employee e = salary.getEmployee();
        SalaryResponseDTO dto = new SalaryResponseDTO();
        dto.setId(salary.getId());
        dto.setEmployeeId(e.getId());
        dto.setEmployeeName(e.getFirstName() + " " + e.getLastName());
        dto.setDepartment(e.getDepartment());
        dto.setAmount(salary.getAmount());
        dto.setEffectiveDate(salary.getEffectiveDate());
        dto.setEndDate(salary.getEndDate());
        dto.setSalaryType(
                salary.getSalaryType() != null ? salary.getSalaryType().name() : null
        );
        dto.setReason(salary.getReason());
        dto.setCurrent(salary.isCurrent());
        dto.setCreatedAt(salary.getCreatedAt());
        dto.setUpdatedAt(salary.getUpdatedAt());
        dto.setMessage(message);
        return dto;
    }

    private SalaryDetailDTO mapToSalaryDetailDTO(Salary salary) {
        SalaryDetailDTO dto = new SalaryDetailDTO();
        dto.setSalaryId(salary.getId());
        dto.setAmount(salary.getAmount());
        dto.setEffectiveDate(salary.getEffectiveDate());
        dto.setEndDate(salary.getEndDate());
        dto.setSalaryType(
                salary.getSalaryType() != null ? salary.getSalaryType().name() : null
        );
        dto.setReason(salary.getReason());
        dto.setCurrent(salary.isCurrent());
        dto.setCreatedAt(salary.getCreatedAt());
        dto.setUpdatedAt(salary.getUpdatedAt());
        return dto;
    }
}