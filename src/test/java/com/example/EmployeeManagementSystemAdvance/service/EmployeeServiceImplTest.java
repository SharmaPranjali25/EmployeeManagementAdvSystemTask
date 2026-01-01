package com.example.EmployeeManagementSystemAdvance.service;

import com.example.EmployeeManagementSystemAdvance.dto.EmployeeRequestDTO;
import com.example.EmployeeManagementSystemAdvance.dto.EmployeeResponseDTO;
import com.example.EmployeeManagementSystemAdvance.dto.EmployeeResponseV2DTO;
import com.example.EmployeeManagementSystemAdvance.entity.Employee;
import com.example.EmployeeManagementSystemAdvance.exception.DuplicateResourceException;
import com.example.EmployeeManagementSystemAdvance.exception.ResourceNotFoundException;
import com.example.EmployeeManagementSystemAdvance.mapper.EmployeeMapper;
import com.example.EmployeeManagementSystemAdvance.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeRequestDTO requestDTO;
    private EmployeeResponseDTO responseDTO;
    private EmployeeResponseV2DTO responseV2DTO;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setDepartment("IT");
        employee.setSalary(50000.0);

        requestDTO = new EmployeeRequestDTO();
        requestDTO.setFirstName("John");
        requestDTO.setLastName("Doe");
        requestDTO.setDepartment("IT");
        requestDTO.setSalary(50000.0);

        responseDTO = new EmployeeResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setFirstName("John");
        responseDTO.setLastName("Doe");
        responseDTO.setDepartment("IT");

        responseV2DTO = new EmployeeResponseV2DTO();
        responseV2DTO.setId(1L);
        responseV2DTO.setFirstName("John");
        responseV2DTO.setLastName("Doe");
        responseV2DTO.setDepartment("IT");
        responseV2DTO.setSalary(50000);
    }

    // ===== V1 Tests =====

    @Test
    void saveEmployeeDTO_Success() {
        when(repository.existsByFirstAndLastNameAndDepartment(anyString(), anyString(), anyString()))
                .thenReturn(false);
        when(employeeMapper.toEntity(requestDTO)).thenReturn(employee);
        when(repository.save(employee)).thenReturn(employee);
        when(employeeMapper.toResponseDTO(employee)).thenReturn(responseDTO);

        EmployeeResponseDTO result = employeeService.saveEmployeeDTO(requestDTO);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(repository).save(employee);
    }

    @Test
    void saveEmployeeDTO_ThrowsDuplicateException() {
        when(repository.existsByFirstAndLastNameAndDepartment(anyString(), anyString(), anyString()))
                .thenReturn(true);

        assertThrows(DuplicateResourceException.class, 
                () -> employeeService.saveEmployeeDTO(requestDTO));
        verify(repository, never()).save(any());
    }

    @Test
    void getAllEmployeesDTO_Success() {
        List<Employee> employees = Arrays.asList(employee);
        when(repository.findAll()).thenReturn(employees);
        when(employeeMapper.toResponseDTO(employee)).thenReturn(responseDTO);

        List<EmployeeResponseDTO> result = employeeService.getAllEmployeesDTO();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }

    @Test
    void getAllEmployeesDTO_ThrowsResourceNotFoundException() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, 
                () -> employeeService.getAllEmployeesDTO());
    }

    @Test
    void getAllEmployeesDTO_ThrowsRuntimeException() {
        when(repository.findAll()).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> employeeService.getAllEmployeesDTO());
        assertTrue(exception.getMessage().contains("Unable to connect to database"));
    }

    @Test
    void getEmployeeByIdDTO_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeMapper.toResponseDTO(employee)).thenReturn(responseDTO);

        EmployeeResponseDTO result = employeeService.getEmployeeByIdDTO(1L);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
    }

    @Test
    void getEmployeeByIdDTO_ThrowsResourceNotFoundException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, 
                () -> employeeService.getEmployeeByIdDTO(1L));
    }

    @Test
    void updateEmployeeDTO_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(employee));
        when(repository.existsByFirstAndLastNameAndDepartmentExcludingId(
                anyLong(), anyString(), anyString(), anyString())).thenReturn(false);
        when(repository.save(employee)).thenReturn(employee);
        when(employeeMapper.toResponseDTO(employee)).thenReturn(responseDTO);
        doNothing().when(modelMapper).map(requestDTO, employee);

        EmployeeResponseDTO result = employeeService.updateEmployeeDTO(1L, requestDTO);

        assertNotNull(result);
        verify(repository).save(employee);
    }

    @Test
    void updateEmployeeDTO_ThrowsResourceNotFoundException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, 
                () -> employeeService.updateEmployeeDTO(1L, requestDTO));
    }

    @Test
    void updateEmployeeDTO_ThrowsDuplicateException() {
        when(repository.findById(1L)).thenReturn(Optional.of(employee));
        when(repository.existsByFirstAndLastNameAndDepartmentExcludingId(
                anyLong(), anyString(), anyString(), anyString())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, 
                () -> employeeService.updateEmployeeDTO(1L, requestDTO));
    }

    @Test
    void deleteEmployee_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(employee));
        doNothing().when(repository).delete(employee);

        assertDoesNotThrow(() -> employeeService.deleteEmployee(1L));
        verify(repository).delete(employee);
    }

    @Test
    void deleteEmployee_ThrowsResourceNotFoundException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, 
                () -> employeeService.deleteEmployee(1L));
    }

    // ===== V2 Tests =====

    @Test
    void saveEmployeeV2_Success() {
        when(repository.existsByFirstAndLastNameAndDepartment(anyString(), anyString(), anyString()))
                .thenReturn(false);
        when(employeeMapper.toEntity(requestDTO)).thenReturn(employee);
        when(repository.save(employee)).thenReturn(employee);
        when(employeeMapper.toResponseV2DTO(employee)).thenReturn(responseV2DTO);

        EmployeeResponseV2DTO result = employeeService.saveEmployeeV2(requestDTO);

        assertNotNull(result);
        assertEquals("Employee added successfully", result.getMessage());
        verify(repository).save(employee);
    }

    @Test
    void saveEmployeeV2_ThrowsDuplicateException() {
        when(repository.existsByFirstAndLastNameAndDepartment(anyString(), anyString(), anyString()))
                .thenReturn(true);

        assertThrows(DuplicateResourceException.class, 
                () -> employeeService.saveEmployeeV2(requestDTO));
    }

    @Test
    void saveEmployeeV2_ThrowsRuntimeException() {
        when(repository.existsByFirstAndLastNameAndDepartment(anyString(), anyString(), anyString()))
                .thenReturn(false);
        when(employeeMapper.toEntity(requestDTO)).thenThrow(new RuntimeException("Mapping error"));

        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> employeeService.saveEmployeeV2(requestDTO));
        assertTrue(exception.getMessage().contains("Failed to save employee"));
    }

    @Test
    void getAllEmployeesV2_Success() {
        List<Employee> employees = Arrays.asList(employee);
        when(repository.findAll()).thenReturn(employees);
        when(employeeMapper.toResponseV2DTO(employee)).thenReturn(responseV2DTO);

        List<EmployeeResponseV2DTO> result = employeeService.getAllEmployeesV2();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getAllEmployeesV2_ThrowsResourceNotFoundException() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, 
                () -> employeeService.getAllEmployeesV2());
    }

    @Test
    void getEmployeeByIdV2_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeMapper.toResponseV2DTO(employee)).thenReturn(responseV2DTO);

        EmployeeResponseV2DTO result = employeeService.getEmployeeByIdV2(1L);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
    }

    @Test
    void getEmployeeByIdV2_ThrowsResourceNotFoundException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, 
                () -> employeeService.getEmployeeByIdV2(1L));
    }

    @Test
    void updateEmployeeV2_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(employee));
        when(repository.existsByFirstAndLastNameAndDepartmentExcludingId(
                anyLong(), anyString(), anyString(), anyString())).thenReturn(false);
        when(repository.save(employee)).thenReturn(employee);
        when(employeeMapper.toResponseV2DTO(employee)).thenReturn(responseV2DTO);
        doNothing().when(modelMapper).map(requestDTO, employee);

        EmployeeResponseV2DTO result = employeeService.updateEmployeeV2(1L, requestDTO);

        assertNotNull(result);
        assertEquals("Employee updated successfully", result.getMessage());
    }

    @Test
    void searchEmployee_Success() {
        List<Employee> employees = Arrays.asList(employee);
        when(repository.searchEmployeeExact(1L, "John", "Doe")).thenReturn(employees);
        when(employeeMapper.toResponseV2DTO(employee)).thenReturn(responseV2DTO);

        EmployeeResponseV2DTO result = employeeService.searchEmployee(1L, "John", "Doe");

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
    }

    @Test
    void searchEmployee_ThrowsIllegalArgumentExceptionWhenNoCriteria() {
        assertThrows(IllegalArgumentException.class, 
                () -> employeeService.searchEmployee(null, null, null));
    }

    @Test
    void searchEmployee_ThrowsResourceNotFoundExceptionWhenEmpty() {
        when(repository.searchEmployeeExact(1L, null, null)).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, 
                () -> employeeService.searchEmployee(1L, null, null));
    }

    @Test
    void searchEmployee_ThrowsIllegalArgumentExceptionWhenMultiple() {
        Employee employee2 = new Employee();
        employee2.setId(2L);
        List<Employee> employees = Arrays.asList(employee, employee2);
        when(repository.searchEmployeeExact(null, "John", null)).thenReturn(employees);

        assertThrows(IllegalArgumentException.class, 
                () -> employeeService.searchEmployee(null, "John", null));
    }

    @Test
    void saveEmployeeDTO_WithNullSalary() {
        EmployeeRequestDTO dtoWithNullSalary = new EmployeeRequestDTO();
        dtoWithNullSalary.setFirstName("Jane");
        dtoWithNullSalary.setLastName("Smith");
        dtoWithNullSalary.setDepartment("HR");
        dtoWithNullSalary.setSalary(null);

        Employee empWithNullSalary = new Employee();
        empWithNullSalary.setId(2L);
        empWithNullSalary.setFirstName("Jane");
        empWithNullSalary.setLastName("Smith");
        empWithNullSalary.setDepartment("HR");
        empWithNullSalary.setSalary(null);

        when(repository.existsByFirstAndLastNameAndDepartment(anyString(), anyString(), anyString()))
                .thenReturn(false);
        when(employeeMapper.toEntity(dtoWithNullSalary)).thenReturn(empWithNullSalary);
        when(repository.save(empWithNullSalary)).thenReturn(empWithNullSalary);
        when(employeeMapper.toResponseDTO(empWithNullSalary)).thenReturn(responseDTO);

        EmployeeResponseDTO result = employeeService.saveEmployeeDTO(dtoWithNullSalary);

        assertNotNull(result);
        verify(repository).save(empWithNullSalary);
    }

    @Test
    void updateEmployeeV2_WithNullSalary() {
        EmployeeRequestDTO dtoWithNullSalary = new EmployeeRequestDTO();
        dtoWithNullSalary.setFirstName("Jane");
        dtoWithNullSalary.setLastName("Smith");
        dtoWithNullSalary.setDepartment("HR");
        dtoWithNullSalary.setSalary(null);

        when(repository.findById(1L)).thenReturn(Optional.of(employee));
        when(repository.existsByFirstAndLastNameAndDepartmentExcludingId(
                anyLong(), anyString(), anyString(), anyString())).thenReturn(false);
        when(repository.save(employee)).thenReturn(employee);
        when(employeeMapper.toResponseV2DTO(employee)).thenReturn(responseV2DTO);
        doNothing().when(modelMapper).map(dtoWithNullSalary, employee);

        EmployeeResponseV2DTO result = employeeService.updateEmployeeV2(1L, dtoWithNullSalary);

        assertNotNull(result);
        verify(repository).save(employee);
    }

    @Test
    void saveEmployeeV2_WithZeroSalary() {
        EmployeeRequestDTO dtoWithZeroSalary = new EmployeeRequestDTO();
        dtoWithZeroSalary.setFirstName("Bob");
        dtoWithZeroSalary.setLastName("Johnson");
        dtoWithZeroSalary.setDepartment("Finance");
        dtoWithZeroSalary.setSalary(0.0);

        Employee empWithZeroSalary = new Employee();
        empWithZeroSalary.setId(3L);
        empWithZeroSalary.setFirstName("Bob");
        empWithZeroSalary.setLastName("Johnson");
        empWithZeroSalary.setDepartment("Finance");
        empWithZeroSalary.setSalary(0.0);

        EmployeeResponseV2DTO responseWithZeroSalary = new EmployeeResponseV2DTO();
        responseWithZeroSalary.setId(3L);
        responseWithZeroSalary.setFirstName("Bob");
        responseWithZeroSalary.setLastName("Johnson");
        responseWithZeroSalary.setDepartment("Finance");
        responseWithZeroSalary.setSalary(0);

        when(repository.existsByFirstAndLastNameAndDepartment(anyString(), anyString(), anyString()))
                .thenReturn(false);
        when(employeeMapper.toEntity(dtoWithZeroSalary)).thenReturn(empWithZeroSalary);
        when(repository.save(empWithZeroSalary)).thenReturn(empWithZeroSalary);
        when(employeeMapper.toResponseV2DTO(empWithZeroSalary)).thenReturn(responseWithZeroSalary);

        EmployeeResponseV2DTO result = employeeService.saveEmployeeV2(dtoWithZeroSalary);

        assertNotNull(result);
        assertEquals(0.0, result.getSalary());
        verify(repository).save(empWithZeroSalary);
    }
}