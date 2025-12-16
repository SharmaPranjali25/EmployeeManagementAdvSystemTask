package com.example.EmployeeManagementSystemAdvance.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.EmployeeManagementSystemAdvance.dto.EmployeeRequestDTO;
import com.example.EmployeeManagementSystemAdvance.dto.EmployeeResponseDTO;
import com.example.EmployeeManagementSystemAdvance.dto.EmployeeResponseV2DTO;
import com.example.EmployeeManagementSystemAdvance.entity.Employee;
import com.example.EmployeeManagementSystemAdvance.exception.DuplicateResourceException;
import com.example.EmployeeManagementSystemAdvance.exception.ResourceNotFoundException;
import com.example.EmployeeManagementSystemAdvance.mapper.EmployeeMapper;
import com.example.EmployeeManagementSystemAdvance.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository repository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeServiceImpl service;

    // ================= V1 TESTS =================
    
    @Test
    void saveEmployeeDTO_success() {
        EmployeeRequestDTO req = new EmployeeRequestDTO();
        req.setFirstName("John");
        req.setLastName("Doe");
        req.setDepartment("IT");

        Employee entity = new Employee();
        Employee saved = new Employee(1L, "John", "Doe", "IT", 1000);

        when(repository.existsByFirstAndLastNameAndDepartment("John", "Doe", "IT")).thenReturn(false);
        when(employeeMapper.toEntity(req)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(employeeMapper.toResponseDTO(saved)).thenReturn(new EmployeeResponseDTO(1L, "John", "Doe", "IT"));

        EmployeeResponseDTO result = service.saveEmployeeDTO(req);

        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("IT", result.getDepartment());
        verify(repository).existsByFirstAndLastNameAndDepartment("John", "Doe", "IT");
        verify(repository).save(entity);
    }

    @Test
    void saveEmployeeDTO_duplicate_throwsCorrectMessage() {
        EmployeeRequestDTO req = new EmployeeRequestDTO();
        req.setFirstName("John");
        req.setLastName("Doe");
        req.setDepartment("IT");

        when(repository.existsByFirstAndLastNameAndDepartment("John", "Doe", "IT")).thenReturn(true);

        DuplicateResourceException exception = assertThrows(
            DuplicateResourceException.class, 
            () -> service.saveEmployeeDTO(req)
        );
        
        assertEquals("Duplicate data alert cannot save", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void getAllEmployeesDTO_success() {
        Employee e1 = new Employee(1L, "A", "Alpha", "IT", 1000);
        Employee e2 = new Employee(2L, "B", "Beta", "HR", 2000);

        when(repository.findAll()).thenReturn(List.of(e1, e2));
        when(employeeMapper.toResponseDTO(e1)).thenReturn(new EmployeeResponseDTO(1L, "A", "Alpha", "IT"));
        when(employeeMapper.toResponseDTO(e2)).thenReturn(new EmployeeResponseDTO(2L, "B", "Beta", "HR"));

        List<EmployeeResponseDTO> list = service.getAllEmployeesDTO();

        assertEquals(2, list.size());
        verify(repository).findAll();
    }

    @Test
    void getAllEmployeesDTO_empty_throwsCorrectMessage() {
        when(repository.findAll()).thenReturn(List.of());
        
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class, 
            () -> service.getAllEmployeesDTO()
        );
        
        assertEquals("No employee exist", exception.getMessage());
    }

    @Test
    void getAllEmployeesDTO_databaseConnectionError() {
        when(repository.findAll()).thenThrow(new RuntimeException("Connection refused"));

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> service.getAllEmployeesDTO()
        );

        assertTrue(exception.getMessage().contains("Unable to connect to database"));
    }

    @Test
    void getEmployeeByIdDTO_found() {
        Employee emp = new Employee(1L, "John", "Doe", "IT", 3000);
        when(repository.findById(1L)).thenReturn(Optional.of(emp));
        when(employeeMapper.toResponseDTO(emp)).thenReturn(new EmployeeResponseDTO(1L, "John", "Doe", "IT"));

        EmployeeResponseDTO dto = service.getEmployeeByIdDTO(1L);
        
        assertNotNull(dto);
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        verify(repository).findById(1L);
    }

    @Test
    void getEmployeeByIdDTO_notFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class, 
            () -> service.getEmployeeByIdDTO(99L)
        );
        
        assertTrue(exception.getMessage().contains("Employee not found with id: 99"));
    }

    @Test
    void updateEmployeeDTO_success() {
        EmployeeRequestDTO req = new EmployeeRequestDTO();
        req.setFirstName("Updated");
        req.setLastName("User");
        req.setDepartment("IT");

        Employee existing = new Employee(1L, "Old", "User", "IT", 1000);
        Employee updated = new Employee(1L, "Updated", "User", "IT", 1000);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.existsByFirstAndLastNameAndDepartmentExcludingId(1L, "Updated", "User", "IT")).thenReturn(false);
        doNothing().when(modelMapper).map(req, existing);
        when(repository.save(existing)).thenReturn(updated);
        when(employeeMapper.toResponseDTO(updated)).thenReturn(new EmployeeResponseDTO(1L, "Updated", "User", "IT"));

        EmployeeResponseDTO result = service.updateEmployeeDTO(1L, req);
        
        assertEquals("Updated", result.getFirstName());
        assertEquals("User", result.getLastName());
        verify(modelMapper).map(req, existing);
        verify(repository).save(existing);
    }

    @Test
    void updateEmployeeDTO_notFound() {
        EmployeeRequestDTO req = new EmployeeRequestDTO();
        when(repository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> service.updateEmployeeDTO(99L, req)
        );

        assertTrue(exception.getMessage().contains("Employee not found with id: 99"));
        verify(repository, never()).save(any());
    }

    @Test
    void updateEmployeeDTO_duplicate_throwsCorrectMessage() {
        EmployeeRequestDTO req = new EmployeeRequestDTO();
        req.setFirstName("John");
        req.setLastName("Doe");
        req.setDepartment("IT");
        
        Employee existing = new Employee(1L, "Old", "User", "IT", 1000);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.existsByFirstAndLastNameAndDepartmentExcludingId(1L, "John", "Doe", "IT")).thenReturn(true);

        DuplicateResourceException exception = assertThrows(
            DuplicateResourceException.class,
            () -> service.updateEmployeeDTO(1L, req)
        );

        assertEquals("Duplicate data alert cannot save", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void deleteEmployee_success() {
        Employee emp = new Employee(1L, "Delete", "User", "IT", 1000);
        when(repository.findById(1L)).thenReturn(Optional.of(emp));
        
        service.deleteEmployee(1L);
        
        verify(repository).delete(emp);
        verify(repository).findById(1L);
    }

    @Test
    void deleteEmployee_notFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> service.deleteEmployee(99L)
        );

        assertTrue(exception.getMessage().contains("Employee not found with id: 99"));
        verify(repository, never()).delete(any());
    }

    // ================= V2 TESTS =================
    
    @Test
    void saveEmployeeV2_success_withMessage() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setFirstName("V2");
        dto.setLastName("User");
        dto.setDepartment("IT");
        dto.setSalary(1000);

        Employee entity = new Employee();
        Employee saved = new Employee(1L, "V2", "User", "IT", 1000);

        when(repository.existsByFirstAndLastNameAndDepartment("V2", "User", "IT")).thenReturn(false);
        when(employeeMapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);

        EmployeeResponseV2DTO responseDto = new EmployeeResponseV2DTO();
        responseDto.setId(1L);
        responseDto.setFirstName("V2");
        responseDto.setLastName("User");
        responseDto.setDepartment("IT");
        responseDto.setSalary(1000);

        when(employeeMapper.toResponseV2DTO(saved)).thenReturn(responseDto);

        EmployeeResponseV2DTO result = service.saveEmployeeV2(dto);
        
        assertEquals("V2", result.getFirstName());
        assertEquals("User", result.getLastName());
        assertEquals("Employee added successfully", result.getMessage());
        verify(repository).save(entity);
    }

    @Test
    void saveEmployeeV2_duplicate_throwsCorrectMessage() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setFirstName("V2");
        dto.setLastName("User");
        dto.setDepartment("IT");

        when(repository.existsByFirstAndLastNameAndDepartment("V2", "User", "IT")).thenReturn(true);

        DuplicateResourceException exception = assertThrows(
            DuplicateResourceException.class,
            () -> service.saveEmployeeV2(dto)
        );

        assertEquals("Duplicate data alert cannot save", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void saveEmployeeV2_runtimeError() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setFirstName("Test");
        dto.setLastName("User");
        dto.setDepartment("IT");

        when(repository.existsByFirstAndLastNameAndDepartment("Test", "User", "IT")).thenReturn(false);
        when(employeeMapper.toEntity(dto)).thenThrow(new RuntimeException("Mapping failed"));

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> service.saveEmployeeV2(dto)
        );

        assertTrue(exception.getMessage().contains("Failed to save employee"));
    }

    @Test
    void getAllEmployeesV2_success() {
        Employee e1 = new Employee(1L, "A", "Alpha", "IT", 1000);
        Employee e2 = new Employee(2L, "B", "Beta", "HR", 2000);

        EmployeeResponseV2DTO dto1 = new EmployeeResponseV2DTO();
        dto1.setId(1L);
        dto1.setFirstName("A");
        
        EmployeeResponseV2DTO dto2 = new EmployeeResponseV2DTO();
        dto2.setId(2L);
        dto2.setFirstName("B");

        when(repository.findAll()).thenReturn(List.of(e1, e2));
        when(employeeMapper.toResponseV2DTO(e1)).thenReturn(dto1);
        when(employeeMapper.toResponseV2DTO(e2)).thenReturn(dto2);

        List<EmployeeResponseV2DTO> list = service.getAllEmployeesV2();
        
        assertEquals(2, list.size());
        assertEquals("A", list.get(0).getFirstName());
        assertEquals("B", list.get(1).getFirstName());
    }

    @Test
    void getAllEmployeesV2_empty_throwsCorrectMessage() {
        when(repository.findAll()).thenReturn(List.of());
        
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> service.getAllEmployeesV2()
        );

        assertEquals("No employee exist", exception.getMessage());
    }

    @Test
    void getAllEmployeesV2_databaseConnectionError() {
        when(repository.findAll()).thenThrow(new RuntimeException("Database timeout"));

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> service.getAllEmployeesV2()
        );

        assertTrue(exception.getMessage().contains("Unable to connect to database"));
    }

    @Test
    void getEmployeeByIdV2_found() {
        Employee emp = new Employee(1L, "John", "Doe", "IT", 3000);
        EmployeeResponseV2DTO responseDto = new EmployeeResponseV2DTO();
        responseDto.setId(1L);
        responseDto.setFirstName("John");

        when(repository.findById(1L)).thenReturn(Optional.of(emp));
        when(employeeMapper.toResponseV2DTO(emp)).thenReturn(responseDto);

        EmployeeResponseV2DTO dto = service.getEmployeeByIdV2(1L);
        
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("John", dto.getFirstName());
    }

    @Test
    void getEmployeeByIdV2_notFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> service.getEmployeeByIdV2(99L)
        );

        assertTrue(exception.getMessage().contains("Employee not found with id: 99"));
    }

    @Test
    void updateEmployeeV2_success_withMessage() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setFirstName("Updated");
        dto.setLastName("User");
        dto.setDepartment("IT");

        Employee existing = new Employee(1L, "Old", "User", "IT", 1000);
        Employee updated = new Employee(1L, "Updated", "User", "IT", 1000);

        EmployeeResponseV2DTO responseDto = new EmployeeResponseV2DTO();
        responseDto.setId(1L);
        responseDto.setFirstName("Updated");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.existsByFirstAndLastNameAndDepartmentExcludingId(1L, "Updated", "User", "IT")).thenReturn(false);
        doNothing().when(modelMapper).map(dto, existing);
        when(repository.save(existing)).thenReturn(updated);
        when(employeeMapper.toResponseV2DTO(updated)).thenReturn(responseDto);

        EmployeeResponseV2DTO result = service.updateEmployeeV2(1L, dto);
        
        assertNotNull(result);
        assertEquals("Employee updated successfully", result.getMessage());
        verify(repository).save(existing);
    }

    @Test
    void updateEmployeeV2_notFound() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        when(repository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> service.updateEmployeeV2(99L, dto)
        );

        assertTrue(exception.getMessage().contains("Employee not found with id: 99"));
    }

    @Test
    void updateEmployeeV2_duplicate_throwsCorrectMessage() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setDepartment("IT");
        
        Employee existing = new Employee(1L, "Old", "User", "IT", 1000);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.existsByFirstAndLastNameAndDepartmentExcludingId(1L, "John", "Doe", "IT")).thenReturn(true);

        DuplicateResourceException exception = assertThrows(
            DuplicateResourceException.class,
            () -> service.updateEmployeeV2(1L, dto)
        );

        assertEquals("Duplicate data alert cannot save", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void searchEmployee_success() {
        Employee emp = new Employee(1L, "John", "Doe", "IT", 1000);
        EmployeeResponseV2DTO responseDto = new EmployeeResponseV2DTO();
        responseDto.setId(1L);
        responseDto.setFirstName("John");

        when(repository.searchEmployeeExact(1L, "John", "Doe")).thenReturn(List.of(emp));
        when(employeeMapper.toResponseV2DTO(emp)).thenReturn(responseDto);

        EmployeeResponseV2DTO result = service.searchEmployee(1L, "John", "Doe");
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
    }

    @Test
    void searchEmployee_notFound() {
        when(repository.searchEmployeeExact(1L, "John", "Doe")).thenReturn(List.of());
        
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> service.searchEmployee(1L, "John", "Doe")
        );

        assertTrue(exception.getMessage().contains("No employee found with given criteria"));
    }

    @Test
    void searchEmployee_multipleFound() {
        Employee emp1 = new Employee(1L, "John", "Doe", "IT", 1000);
        Employee emp2 = new Employee(2L, "John", "Doe", "IT", 2000);
        
        when(repository.searchEmployeeExact(1L, "John", "Doe")).thenReturn(List.of(emp1, emp2));
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.searchEmployee(1L, "John", "Doe")
        );

        assertTrue(exception.getMessage().contains("Multiple employees found"));
    }

    @Test
    void searchEmployee_withNullParameters() {
        Employee emp = new Employee(1L, "John", "Doe", "IT", 1000);
        EmployeeResponseV2DTO responseDto = new EmployeeResponseV2DTO();
        
        when(repository.searchEmployeeExact(null, null, "Doe")).thenReturn(List.of(emp));
        when(employeeMapper.toResponseV2DTO(emp)).thenReturn(responseDto);

        EmployeeResponseV2DTO result = service.searchEmployee(null, null, "Doe");
        
        assertNotNull(result);
        verify(repository).searchEmployeeExact(null, null, "Doe");
    }

    @Test
    void searchEmployee_withOnlyId() {
        Employee emp = new Employee(1L, "John", "Doe", "IT", 1000);
        EmployeeResponseV2DTO responseDto = new EmployeeResponseV2DTO();
        
        when(repository.searchEmployeeExact(1L, null, null)).thenReturn(List.of(emp));
        when(employeeMapper.toResponseV2DTO(emp)).thenReturn(responseDto);

        EmployeeResponseV2DTO result = service.searchEmployee(1L, null, null);
        
        assertNotNull(result);
        verify(repository).searchEmployeeExact(1L, null, null);
    }

    @Test
    void searchEmployee_withOnlyNames() {
        Employee emp = new Employee(1L, "John", "Doe", "IT", 1000);
        EmployeeResponseV2DTO responseDto = new EmployeeResponseV2DTO();
        
        when(repository.searchEmployeeExact(null, "John", "Doe")).thenReturn(List.of(emp));
        when(employeeMapper.toResponseV2DTO(emp)).thenReturn(responseDto);

        EmployeeResponseV2DTO result = service.searchEmployee(null, "John", "Doe");
        
        assertNotNull(result);
        verify(repository).searchEmployeeExact(null, "John", "Doe");
    }
}