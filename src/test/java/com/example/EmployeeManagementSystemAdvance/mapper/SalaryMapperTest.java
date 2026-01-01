package com.example.EmployeeManagementSystemAdvance.mapper;

import com.example.EmployeeManagementSystemAdvance.dto.*;
import com.example.EmployeeManagementSystemAdvance.entity.Employee;
import com.example.EmployeeManagementSystemAdvance.entity.Salary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SalaryMapperTest {

    private SalaryMapper mapper;
    private Employee employee;
    private Salary salary;
    private SalaryRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        mapper = new SalaryMapper();

        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setDepartment("IT");
        employee.setSalary(50000.0);

        salary = new Salary();
        salary.setId(1L);
        salary.setEmployee(employee);
        salary.setAmount(50000.0);
        salary.setEffectiveDate(LocalDate.of(2024, 1, 1));
        salary.setEndDate(LocalDate.of(2024, 12, 31));
        salary.setSalaryType(Salary.SalaryType.BASE_SALARY);
        salary.setReason("Initial salary");
        salary.setCurrent(true);

        requestDTO = new SalaryRequestDTO();
        requestDTO.setEmployeeId(1L);
        requestDTO.setAmount(50000.0);  // Changed to Double
        requestDTO.setEffectiveDate(LocalDate.of(2024, 1, 1));
        requestDTO.setSalaryType(Salary.SalaryType.BASE_SALARY);
        requestDTO.setReason("Initial salary");
    }

    @Test
    void toEntity_Success() {
        Salary result = mapper.toEntity(requestDTO, employee);

        assertNotNull(result);
        assertEquals(employee, result.getEmployee());
        // Amount is not set in mapper anymore - it's set in service layer
        // So we don't check amount here
        assertEquals(LocalDate.of(2024, 1, 1), result.getEffectiveDate());
        assertEquals(Salary.SalaryType.BASE_SALARY, result.getSalaryType());
        assertEquals("Initial salary", result.getReason());
        assertTrue(result.isCurrent());
    }

    @Test
    void toEntity_NullInput() {
        Salary result = mapper.toEntity(null, employee);
        assertNull(result);
    }

    @Test
    void toResponseDTO_Success() {
        SalaryResponseDTO result = mapper.toResponseDTO(salary);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getEmployeeId());
        assertEquals("John Doe", result.getEmployeeName());
        assertEquals("IT", result.getDepartment());
        assertEquals(50000.0, result.getAmount());
        assertEquals(LocalDate.of(2024, 1, 1), result.getEffectiveDate());
        assertEquals(LocalDate.of(2024, 12, 31), result.getEndDate());
        assertEquals("BASE_SALARY", result.getSalaryType());
        assertEquals("Initial salary", result.getReason());
        assertTrue(result.isCurrent());
    }

    @Test
    void toResponseDTO_NullInput() {
        SalaryResponseDTO result = mapper.toResponseDTO(null);
        assertNull(result);
    }

    @Test
    void toDetailDTO_Success() {
        SalaryDetailDTO result = mapper.toDetailDTO(salary);

        assertNotNull(result);
        assertEquals(1L, result.getSalaryId());
        assertEquals(50000.0, result.getAmount());
        assertEquals(LocalDate.of(2024, 1, 1), result.getEffectiveDate());
        assertEquals(LocalDate.of(2024, 12, 31), result.getEndDate());
        assertEquals("BASE_SALARY", result.getSalaryType());
        assertEquals("Initial salary", result.getReason());
        assertTrue(result.isCurrent());
    }

    @Test
    void toDetailDTO_NullInput() {
        SalaryDetailDTO result = mapper.toDetailDTO(null);
        assertNull(result);
    }

    @Test
    void toHistoryDTO_Success() {
        List<Salary> salaries = Arrays.asList(salary);
        SalaryHistoryDTO result = mapper.toHistoryDTO(employee, salaries);

        assertNotNull(result);
        assertEquals(1L, result.getEmployeeId());
        assertEquals("John Doe", result.getEmployeeName());
        assertEquals("IT", result.getDepartment());
        assertEquals(50000.0, result.getCurrentSalary());
        assertNotNull(result.getSalaryHistory());
        assertEquals(1, result.getSalaryHistory().size());
    }

    @Test
    void toHistoryDTO_NullEmployee() {
        SalaryHistoryDTO result = mapper.toHistoryDTO(null, Arrays.asList(salary));
        assertNull(result);
    }

    @Test
    void toHistoryDTO_NullSalaries() {
        SalaryHistoryDTO result = mapper.toHistoryDTO(employee, null);

        assertNotNull(result);
        assertEquals(1L, result.getEmployeeId());
        assertNull(result.getSalaryHistory());
    }
}