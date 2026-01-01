package com.example.EmployeeManagementSystemAdvance.service;

import com.example.EmployeeManagementSystemAdvance.dto.*;
import com.example.EmployeeManagementSystemAdvance.entity.Employee;
import com.example.EmployeeManagementSystemAdvance.entity.Salary;
import com.example.EmployeeManagementSystemAdvance.exception.ResourceNotFoundException;
import com.example.EmployeeManagementSystemAdvance.repository.EmployeeRepository;
import com.example.EmployeeManagementSystemAdvance.repository.SalaryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalaryServiceImplTest {

    @Mock
    private SalaryRepository salaryRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private SalaryServiceImpl salaryService;

    private Employee employee;
    private Salary salary;
    private SalaryRequestDTO requestDTO;
    private SalaryResponseDTO responseDTO;
    private SalaryIncrementRequestDTO incrementDTO;

    @BeforeEach
    void setUp() {
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
        salary.setSalaryType(Salary.SalaryType.BASE_SALARY);
        salary.setCurrent(true);
        salary.setCreatedAt(LocalDateTime.now());
        salary.setUpdatedAt(LocalDateTime.now());

        requestDTO = new SalaryRequestDTO();
        requestDTO.setEmployeeId(1L);
        requestDTO.setAmount(50000.0);
        requestDTO.setEffectiveDate(LocalDate.of(2024, 1, 1));
        requestDTO.setSalaryType(Salary.SalaryType.BASE_SALARY);
        requestDTO.setReason("Initial salary");

        responseDTO = new SalaryResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setEmployeeId(1L);
        responseDTO.setEmployeeName("John Doe");
        responseDTO.setDepartment("IT");
        responseDTO.setAmount(50000.0);
        responseDTO.setCurrent(true);

        incrementDTO = new SalaryIncrementRequestDTO();
        incrementDTO.setEmployeeId(1L);
        incrementDTO.setIncrementPercentage(10.0);
        incrementDTO.setEffectiveDate(LocalDate.of(2024, 7, 1));
        incrementDTO.setReason("Annual increment");
    }

    @Test
    void createSalary_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(salaryRepository.findByEmployeeAndCurrent(employee, true)).thenReturn(Collections.emptyList());
        when(salaryRepository.save(any(Salary.class))).thenReturn(salary);
        when(employeeRepository.save(employee)).thenReturn(employee);

        SalaryResponseDTO result = salaryService.createSalary(requestDTO);

        assertNotNull(result);
        assertEquals("Salary record created successfully", result.getMessage());
        verify(salaryRepository).save(any(Salary.class));
        verify(employeeRepository).save(employee);
    }
    
    @Test
    void createSalary_WithNullAmount_UsesEmployeeSalary() {
        requestDTO.setAmount(null);
        
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(salaryRepository.findByEmployeeAndCurrent(employee, true)).thenReturn(Collections.emptyList());
        when(salaryRepository.save(any(Salary.class))).thenReturn(salary);
        when(employeeRepository.save(employee)).thenReturn(employee);

        SalaryResponseDTO result = salaryService.createSalary(requestDTO);

        assertNotNull(result);
        assertEquals(50000.0, result.getAmount());
        verify(salaryRepository).save(any(Salary.class));
    }
    
    @Test
    void createSalary_NoAmountAndNoEmployeeSalary_ThrowsException() {
        requestDTO.setAmount(null);
        employee.setSalary(null);
        
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        assertThrows(IllegalArgumentException.class, 
                () -> salaryService.createSalary(requestDTO));
    }

    @Test
    void createSalary_WithExistingCurrentSalary() {
        Salary oldSalary = new Salary();
        oldSalary.setId(2L);
        oldSalary.setCurrent(true);
        oldSalary.setEmployee(employee);
        oldSalary.setAmount(45000.0);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(salaryRepository.findByEmployeeAndCurrent(employee, true))
                .thenReturn(Arrays.asList(oldSalary));
        when(salaryRepository.save(any(Salary.class))).thenReturn(salary);
        when(employeeRepository.save(employee)).thenReturn(employee);

        SalaryResponseDTO result = salaryService.createSalary(requestDTO);

        assertNotNull(result);
        assertFalse(oldSalary.isCurrent());
        assertNotNull(oldSalary.getEndDate());
        verify(salaryRepository, times(2)).save(any(Salary.class));
    }

    @Test
    void createSalary_ThrowsResourceNotFoundException() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, 
                () -> salaryService.createSalary(requestDTO));
    }

    @Test
    void getSalaryById_Success() {
        when(salaryRepository.findByIdWithEmployee(1L)).thenReturn(Optional.of(salary));

        SalaryResponseDTO result = salaryService.getSalaryById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getEmployeeName());
    }

    @Test
    void getSalaryById_ThrowsResourceNotFoundException() {
        when(salaryRepository.findByIdWithEmployee(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, 
                () -> salaryService.getSalaryById(1L));
    }

    @Test
    void getAllSalaries_Success() {
        List<Salary> salaries = Arrays.asList(salary);
        when(salaryRepository.findAllWithEmployee()).thenReturn(salaries);

        List<SalaryResponseDTO> result = salaryService.getAllSalaries();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getAllSalaries_ThrowsResourceNotFoundException() {
        when(salaryRepository.findAllWithEmployee()).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, 
                () -> salaryService.getAllSalaries());
    }

    @Test
    void getAllCurrentSalaries_Success() {
        List<Salary> salaries = Arrays.asList(salary);
        when(salaryRepository.findAllCurrentSalariesWithEmployee()).thenReturn(salaries);

        List<SalaryResponseDTO> result = salaryService.getAllCurrentSalaries();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getEmployeeSalaryHistory_Success() {
        List<Salary> salaries = Arrays.asList(salary);
        
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(salaryRepository.findByEmployeeOrderByEffectiveDateDescWithEmployee(employee))
                .thenReturn(salaries);

        SalaryHistoryDTO result = salaryService.getEmployeeSalaryHistory(1L);

        assertNotNull(result);
        assertEquals(1L, result.getEmployeeId());
        assertEquals("John Doe", result.getEmployeeName());
        assertEquals("IT", result.getDepartment());
        verify(salaryRepository).findByEmployeeOrderByEffectiveDateDescWithEmployee(employee);
    }

    @Test
    void getEmployeeSalaryHistory_ThrowsResourceNotFoundException() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, 
                () -> salaryService.getEmployeeSalaryHistory(1L));
    }

    @Test
    void getEmployeeSalaryHistory_NoSalaryHistory() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(salaryRepository.findByEmployeeOrderByEffectiveDateDescWithEmployee(employee))
                .thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, 
                () -> salaryService.getEmployeeSalaryHistory(1L));
    }

    @Test
    void getCurrentSalaryByEmployeeId_Success() {
        when(salaryRepository.findCurrentSalaryByEmployeeIdWithEmployee(1L))
                .thenReturn(Optional.of(salary));

        SalaryResponseDTO result = salaryService.getCurrentSalaryByEmployeeId(1L);

        assertNotNull(result);
        assertTrue(result.isCurrent());
        assertEquals("John Doe", result.getEmployeeName());
    }

    @Test
    void getCurrentSalaryByEmployeeId_ThrowsResourceNotFoundException() {
        when(salaryRepository.findCurrentSalaryByEmployeeIdWithEmployee(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, 
                () -> salaryService.getCurrentSalaryByEmployeeId(1L));
    }

    @Test
    void processSalaryIncrement_WithPercentage() {
        Salary newSalary = new Salary();
        newSalary.setId(2L);
        newSalary.setEmployee(employee);
        newSalary.setAmount(55000.0);
        newSalary.setCurrent(true);
        newSalary.setCreatedAt(LocalDateTime.now());
        newSalary.setUpdatedAt(LocalDateTime.now());
        
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(salaryRepository.findByEmployeeAndCurrent(employee, true))
                .thenReturn(Arrays.asList(salary));
        when(salaryRepository.save(any(Salary.class))).thenReturn(newSalary);
        when(employeeRepository.save(employee)).thenReturn(employee);

        SalaryResponseDTO result = salaryService.processSalaryIncrement(incrementDTO);

        assertNotNull(result);
        assertFalse(salary.isCurrent());
        verify(salaryRepository, times(2)).save(any(Salary.class));
    }

    @Test
    void processSalaryIncrement_WithFixedAmount() {
        incrementDTO.setIncrementPercentage(null);
        incrementDTO.setIncrementAmount(5000.0);
        
        Salary newSalary = new Salary();
        newSalary.setId(2L);
        newSalary.setEmployee(employee);
        newSalary.setAmount(55000.0);
        newSalary.setCurrent(true);
        
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(salaryRepository.findByEmployeeAndCurrent(employee, true))
                .thenReturn(Arrays.asList(salary));
        when(salaryRepository.save(any(Salary.class))).thenReturn(newSalary);
        when(employeeRepository.save(employee)).thenReturn(employee);

        SalaryResponseDTO result = salaryService.processSalaryIncrement(incrementDTO);

        assertNotNull(result);
        verify(salaryRepository, times(2)).save(any(Salary.class));
    }

    @Test
    void processSalaryIncrement_EmployeeNotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, 
                () -> salaryService.processSalaryIncrement(incrementDTO));
    }

    @Test
    void processSalaryIncrement_NoCurrentSalary() {
        employee.setSalary(null);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        assertThrows(IllegalArgumentException.class, 
                () -> salaryService.processSalaryIncrement(incrementDTO));
    }

    @Test
    void processSalaryIncrement_NoIncrementSpecified() {
        incrementDTO.setIncrementPercentage(null);
        incrementDTO.setIncrementAmount(null);
        
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        assertThrows(IllegalArgumentException.class, 
                () -> salaryService.processSalaryIncrement(incrementDTO));
    }

    @Test
    void updateSalary_CurrentSalary() {
        salary.setCurrent(true);
        when(salaryRepository.findById(1L)).thenReturn(Optional.of(salary));
        when(salaryRepository.save(salary)).thenReturn(salary);
        when(employeeRepository.save(employee)).thenReturn(employee);

        SalaryResponseDTO result = salaryService.updateSalary(1L, requestDTO);

        assertNotNull(result);
        assertEquals("Salary record updated successfully", result.getMessage());
        verify(employeeRepository).save(employee);
    }

    @Test
    void updateSalary_NonCurrentSalary() {
        salary.setCurrent(false);
        when(salaryRepository.findById(1L)).thenReturn(Optional.of(salary));
        when(salaryRepository.save(salary)).thenReturn(salary);

        SalaryResponseDTO result = salaryService.updateSalary(1L, requestDTO);

        assertNotNull(result);
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void updateSalary_WithNullAmount_UsesEmployeeSalary() {
        requestDTO.setAmount(null);
        salary.setCurrent(true);
        
        when(salaryRepository.findById(1L)).thenReturn(Optional.of(salary));
        when(salaryRepository.save(salary)).thenReturn(salary);
        when(employeeRepository.save(employee)).thenReturn(employee);

        SalaryResponseDTO result = salaryService.updateSalary(1L, requestDTO);

        assertNotNull(result);
        assertEquals(50000.0, result.getAmount());
        verify(salaryRepository).save(salary);
        verify(employeeRepository).save(employee);
    }

    @Test
    void updateSalary_NoAmountAndNoEmployeeSalary_ThrowsException() {
        requestDTO.setAmount(null);
        employee.setSalary(null);
        
        when(salaryRepository.findById(1L)).thenReturn(Optional.of(salary));

        assertThrows(IllegalArgumentException.class, 
                () -> salaryService.updateSalary(1L, requestDTO));
    }

    @Test
    void updateSalary_ThrowsResourceNotFoundException() {
        when(salaryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, 
                () -> salaryService.updateSalary(1L, requestDTO));
    }

    @Test
    void getSalariesByDepartment_Success() {
        List<Salary> salaries = Arrays.asList(salary);
        when(salaryRepository.findCurrentSalariesByDepartmentWithEmployee("IT"))
                .thenReturn(salaries);

        List<SalaryResponseDTO> result = salaryService.getSalariesByDepartment("IT");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getDepartmentSalaryStatistics_Success() {
        Object[] stats = {"IT", 5L, 55000.0, 45000.0, 70000.0, 275000.0};
        List<Object[]> statsList = Collections.singletonList(stats);
        
        when(salaryRepository.getDepartmentSalaryStatistics()).thenReturn(statsList);

        List<DepartmentSalaryStatsDTO> result = salaryService.getDepartmentSalaryStatistics();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("IT", result.get(0).getDepartment());
        assertEquals(5L, result.get(0).getEmployeeCount());
        assertEquals(55000.0, result.get(0).getAverageSalary());
    }

    @Test
    void getSalariesInDateRange_Success() {
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 12, 31);
        List<Salary> salaries = Arrays.asList(salary);
        
        when(salaryRepository.findSalariesInDateRangeWithEmployee(start, end))
                .thenReturn(salaries);

        List<SalaryResponseDTO> result = salaryService.getSalariesInDateRange(start, end);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getTotalSalaryExpenditure_WithData() {
        when(salaryRepository.getTotalSalaryExpenditure()).thenReturn(275000.0);

        Double result = salaryService.getTotalSalaryExpenditure();

        assertNotNull(result);
        assertEquals(275000.0, result);
    }

    @Test
    void getTotalSalaryExpenditure_WithNullData() {
        when(salaryRepository.getTotalSalaryExpenditure()).thenReturn(null);

        Double result = salaryService.getTotalSalaryExpenditure();

        assertNotNull(result);
        assertEquals(0.0, result);
    }

    @Test
    void deleteSalary_Success() {
        salary.setCurrent(false);
        when(salaryRepository.findById(1L)).thenReturn(Optional.of(salary));
        doNothing().when(salaryRepository).delete(salary);

        assertDoesNotThrow(() -> salaryService.deleteSalary(1L));
        verify(salaryRepository).delete(salary);
    }

    @Test
    void deleteSalary_CurrentSalary_RestoresPrevious() {
        salary.setCurrent(true);
        
        Salary previousSalary = new Salary();
        previousSalary.setId(2L);
        previousSalary.setEmployee(employee);
        previousSalary.setAmount(45000.0);
        previousSalary.setCurrent(false);
        previousSalary.setEndDate(LocalDate.now().minusDays(1));
        
        when(salaryRepository.findById(1L)).thenReturn(Optional.of(salary));
        when(salaryRepository.findByEmployeeOrderByEffectiveDateDesc(employee))
                .thenReturn(Arrays.asList(salary, previousSalary));
        when(salaryRepository.save(previousSalary)).thenReturn(previousSalary);
        when(employeeRepository.save(employee)).thenReturn(employee);
        doNothing().when(salaryRepository).delete(salary);

        assertDoesNotThrow(() -> salaryService.deleteSalary(1L));
        
        assertTrue(previousSalary.isCurrent());
        assertNull(previousSalary.getEndDate());
        verify(salaryRepository).delete(salary);
    }

    @Test
    void deleteSalary_ThrowsResourceNotFoundException() {
        when(salaryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, 
                () -> salaryService.deleteSalary(1L));
    }
}