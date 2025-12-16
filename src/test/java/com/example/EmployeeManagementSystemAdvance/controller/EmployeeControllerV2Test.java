package com.example.EmployeeManagementSystemAdvance.controller;

import com.example.EmployeeManagementSystemAdvance.dto.EmployeeRequestDTO;
import com.example.EmployeeManagementSystemAdvance.dto.EmployeeResponseV2DTO;
import com.example.EmployeeManagementSystemAdvance.exception.DuplicateResourceException;
import com.example.EmployeeManagementSystemAdvance.exception.ResourceNotFoundException;
import com.example.EmployeeManagementSystemAdvance.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerV2Test {

    @Mock
    private EmployeeService service;

    @InjectMocks
    private EmployeeControllerV2 controller;

    // ---------------- ADD EMPLOYEE V2 ----------------
    
    @Test
    void addEmployeeV2_success_withMessage() {
        EmployeeRequestDTO req = new EmployeeRequestDTO();
        req.setFirstName("Armaan");
        req.setLastName("Khan");
        req.setDepartment("IT");
        req.setSalary(5000.0);

        EmployeeResponseV2DTO resp = new EmployeeResponseV2DTO();
        resp.setId(1L);
        resp.setFirstName("Armaan");
        resp.setLastName("Khan");
        resp.setDepartment("IT");
        resp.setSalary(5000);
        resp.setMessage("Employee added successfully");

        when(service.saveEmployeeV2(any(EmployeeRequestDTO.class))).thenReturn(resp);

        ResponseEntity<EmployeeResponseV2DTO> result = controller.addEmployee(req);

        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode()); // Should be CREATED after update
        assertNotNull(result.getBody());
        assertEquals(1L, result.getBody().getId());
        assertEquals("Armaan", result.getBody().getFirstName());
        assertEquals("Khan", result.getBody().getLastName());
        assertEquals("IT", result.getBody().getDepartment());
        assertEquals(5000, result.getBody().getSalary());
        assertEquals("Employee added successfully", result.getBody().getMessage());
        verify(service).saveEmployeeV2(any(EmployeeRequestDTO.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void addEmployeeV2_duplicate() {
        EmployeeRequestDTO req = new EmployeeRequestDTO();
        req.setFirstName("John");
        req.setLastName("Doe");
        req.setDepartment("IT");

        when(service.saveEmployeeV2(any(EmployeeRequestDTO.class)))
                .thenThrow(new DuplicateResourceException("Duplicate data alert cannot save"));

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> controller.addEmployee(req)
        );

        assertEquals("Duplicate data alert cannot save", exception.getMessage());
        verify(service).saveEmployeeV2(any(EmployeeRequestDTO.class));
    }

    @Test
    void addEmployeeV2_withNullFields() {
        EmployeeRequestDTO req = new EmployeeRequestDTO();
        req.setFirstName(null);
        req.setLastName(null);
        req.setDepartment(null);
        req.setSalary(0.0);

        EmployeeResponseV2DTO resp = new EmployeeResponseV2DTO();
        resp.setId(1L);
        resp.setMessage("Employee added successfully");

        when(service.saveEmployeeV2(any(EmployeeRequestDTO.class))).thenReturn(resp);

        ResponseEntity<EmployeeResponseV2DTO> result = controller.addEmployee(req);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Employee added successfully", result.getBody().getMessage());
        verify(service).saveEmployeeV2(any(EmployeeRequestDTO.class));
    }

    @Test
    void addEmployeeV2_withFullName() {
        EmployeeRequestDTO req = new EmployeeRequestDTO("John", "Doe", "IT", 5000.0);

        EmployeeResponseV2DTO resp = new EmployeeResponseV2DTO();
        resp.setId(1L);
        resp.setFirstName("John");
        resp.setLastName("Doe");

        when(service.saveEmployeeV2(any())).thenReturn(resp);

        ResponseEntity<EmployeeResponseV2DTO> result = controller.addEmployee(req);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("John Doe", result.getBody().getFullName());
        verify(service).saveEmployeeV2(any());
    }

    @Test
    void addEmployeeV2_runtimeError() {
        EmployeeRequestDTO req = new EmployeeRequestDTO();
        req.setFirstName("Test");

        when(service.saveEmployeeV2(any()))
                .thenThrow(new RuntimeException("Failed to save employee"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> controller.addEmployee(req)
        );

        assertTrue(exception.getMessage().contains("Failed to save employee"));
        verify(service).saveEmployeeV2(any());
    }

    // ---------------- GET ALL EMPLOYEES V2 ----------------
    
    @Test
    void getAllEmployeesV2_success() {
        EmployeeResponseV2DTO r1 = new EmployeeResponseV2DTO();
        r1.setId(1L);
        r1.setFirstName("John");
        r1.setLastName("Doe");
        r1.setDepartment("IT");
        r1.setSalary(5000);

        EmployeeResponseV2DTO r2 = new EmployeeResponseV2DTO();
        r2.setId(2L);
        r2.setFirstName("Alice");
        r2.setLastName("Smith");
        r2.setDepartment("HR");
        r2.setSalary(6000);

        when(service.getAllEmployeesV2()).thenReturn(List.of(r1, r2));

        ResponseEntity<List<EmployeeResponseV2DTO>> result = controller.getAllEmployees();

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(2, result.getBody().size());
        
        assertEquals(1L, result.getBody().get(0).getId());
        assertEquals("John", result.getBody().get(0).getFirstName());
        assertEquals("Doe", result.getBody().get(0).getLastName());
        assertEquals("IT", result.getBody().get(0).getDepartment());
        assertEquals(5000, result.getBody().get(0).getSalary());
        
        assertEquals(2L, result.getBody().get(1).getId());
        assertEquals("Alice", result.getBody().get(1).getFirstName());
        assertEquals("Smith", result.getBody().get(1).getLastName());
        assertEquals("HR", result.getBody().get(1).getDepartment());
        assertEquals(6000, result.getBody().get(1).getSalary());
        
        verify(service).getAllEmployeesV2();
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAllEmployeesV2_singleEmployee() {
        EmployeeResponseV2DTO resp = new EmployeeResponseV2DTO();
        resp.setId(1L);
        resp.setFirstName("Solo");
        resp.setLastName("Employee");

        when(service.getAllEmployeesV2()).thenReturn(Collections.singletonList(resp));

        ResponseEntity<List<EmployeeResponseV2DTO>> result = controller.getAllEmployees();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        assertEquals("Solo", result.getBody().get(0).getFirstName());
        verify(service).getAllEmployeesV2();
    }

    @Test
    void getAllEmployeesV2_noEmployeesExist() {
        when(service.getAllEmployeesV2())
                .thenThrow(new ResourceNotFoundException("No employee exist"));

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> controller.getAllEmployees()
        );

        assertEquals("No employee exist", exception.getMessage());
        verify(service).getAllEmployeesV2();
    }

    @Test
    void getAllEmployeesV2_databaseError() {
        when(service.getAllEmployeesV2())
                .thenThrow(new RuntimeException("Unable to connect to database"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> controller.getAllEmployees()
        );

        assertTrue(exception.getMessage().contains("Unable to connect to database"));
        verify(service).getAllEmployeesV2();
    }

    @Test
    void getAllEmployeesV2_withFullNames() {
        EmployeeResponseV2DTO r1 = new EmployeeResponseV2DTO();
        r1.setFirstName("John");
        r1.setLastName("Doe");

        EmployeeResponseV2DTO r2 = new EmployeeResponseV2DTO();
        r2.setFirstName("Jane");
        r2.setLastName("Smith");

        when(service.getAllEmployeesV2()).thenReturn(List.of(r1, r2));

        ResponseEntity<List<EmployeeResponseV2DTO>> result = controller.getAllEmployees();

        assertEquals("John Doe", result.getBody().get(0).getFullName());
        assertEquals("Jane Smith", result.getBody().get(1).getFullName());
        verify(service).getAllEmployeesV2();
    }

    // ---------------- GET BY ID V2 ----------------
    
    @Test
    void getEmployeeV2_found() {
        EmployeeResponseV2DTO resp = new EmployeeResponseV2DTO();
        resp.setId(1L);
        resp.setFirstName("Armaan");
        resp.setLastName("Khan");
        resp.setDepartment("IT");
        resp.setSalary(5000);

        when(service.getEmployeeByIdV2(1L)).thenReturn(resp);

        ResponseEntity<EmployeeResponseV2DTO> result = controller.getEmployee(1L);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1L, result.getBody().getId());
        assertEquals("Armaan", result.getBody().getFirstName());
        assertEquals("Khan", result.getBody().getLastName());
        assertEquals("IT", result.getBody().getDepartment());
        assertEquals(5000, result.getBody().getSalary());
        assertEquals("Armaan Khan", result.getBody().getFullName());
        verify(service).getEmployeeByIdV2(1L);
        verifyNoMoreInteractions(service);
    }

    @Test
    void getEmployeeV2_notFound() {
        when(service.getEmployeeByIdV2(99L))
                .thenThrow(new ResourceNotFoundException("Employee not found with id: 99"));

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> controller.getEmployee(99L)
        );

        assertTrue(exception.getMessage().contains("Employee not found with id: 99"));
        verify(service).getEmployeeByIdV2(99L);
        verifyNoMoreInteractions(service);
    }

    @Test
    void getEmployeeV2_withZeroId() {
        when(service.getEmployeeByIdV2(0L))
                .thenThrow(new ResourceNotFoundException("Employee not found with id: 0"));

        assertThrows(ResourceNotFoundException.class,
                () -> controller.getEmployee(0L));
        verify(service).getEmployeeByIdV2(0L);
    }

    @Test
    void getEmployeeV2_withNegativeId() {
        when(service.getEmployeeByIdV2(-1L))
                .thenThrow(new ResourceNotFoundException("Employee not found with id: -1"));

        assertThrows(ResourceNotFoundException.class,
                () -> controller.getEmployee(-1L));
        verify(service).getEmployeeByIdV2(-1L);
    }

    // ---------------- UPDATE V2 ----------------
    
    @Test
    void updateEmployeeV2_success_withMessage() {
        EmployeeRequestDTO req = new EmployeeRequestDTO();
        req.setFirstName("Updated");
        req.setLastName("User");
        req.setDepartment("HR");
        req.setSalary(6000.0);

        EmployeeResponseV2DTO resp = new EmployeeResponseV2DTO();
        resp.setId(1L);
        resp.setFirstName("Updated");
        resp.setLastName("User");
        resp.setDepartment("HR");
        resp.setSalary(6000);
        resp.setMessage("Employee updated successfully");

        when(service.updateEmployeeV2(eq(1L), any(EmployeeRequestDTO.class))).thenReturn(resp);

        ResponseEntity<EmployeeResponseV2DTO> result = controller.updateEmployee(1L, req);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1L, result.getBody().getId());
        assertEquals("Updated", result.getBody().getFirstName());
        assertEquals("User", result.getBody().getLastName());
        assertEquals("HR", result.getBody().getDepartment());
        assertEquals(6000, result.getBody().getSalary());
        assertEquals("Employee updated successfully", result.getBody().getMessage());
        verify(service).updateEmployeeV2(eq(1L), any(EmployeeRequestDTO.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void updateEmployeeV2_notFound() {
        EmployeeRequestDTO req = new EmployeeRequestDTO();
        req.setFirstName("Updated");

        when(service.updateEmployeeV2(eq(99L), any()))
                .thenThrow(new ResourceNotFoundException("Employee not found with id: 99"));

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> controller.updateEmployee(99L, req)
        );

        assertTrue(exception.getMessage().contains("Employee not found with id: 99"));
        verify(service).updateEmployeeV2(eq(99L), any());
    }

    @Test
    void updateEmployeeV2_duplicate() {
        EmployeeRequestDTO req = new EmployeeRequestDTO();
        req.setFirstName("John");
        req.setLastName("Doe");

        when(service.updateEmployeeV2(eq(1L), any()))
                .thenThrow(new DuplicateResourceException("Duplicate data alert cannot save"));

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> controller.updateEmployee(1L, req)
        );

        assertEquals("Duplicate data alert cannot save", exception.getMessage());
        verify(service).updateEmployeeV2(eq(1L), any());
    }

    @Test
    void updateEmployeeV2_withNullFields() {
        EmployeeRequestDTO req = new EmployeeRequestDTO();
        req.setFirstName(null);
        req.setLastName(null);

        EmployeeResponseV2DTO resp = new EmployeeResponseV2DTO();
        resp.setId(1L);
        resp.setMessage("Employee updated successfully");

        when(service.updateEmployeeV2(eq(1L), any())).thenReturn(resp);

        ResponseEntity<EmployeeResponseV2DTO> result = controller.updateEmployee(1L, req);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Employee updated successfully", result.getBody().getMessage());
        verify(service).updateEmployeeV2(eq(1L), any());
    }

    // ---------------- DELETE V2 ----------------
    
    @Test
    void deleteEmployeeV2_success() {
        doNothing().when(service).deleteEmployee(1L);

        ResponseEntity<String> result = controller.deleteEmployee(1L);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Employee deleted successfully", result.getBody());
        verify(service).deleteEmployee(1L);
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteEmployeeV2_notFound() {
        doThrow(new ResourceNotFoundException("Employee not found with id: 99"))
                .when(service).deleteEmployee(99L);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> controller.deleteEmployee(99L)
        );

        assertTrue(exception.getMessage().contains("Employee not found with id: 99"));
        verify(service).deleteEmployee(99L);
    }

    @Test
    void deleteEmployeeV2_alreadyDeleted() {
        doThrow(new ResourceNotFoundException("Employee not found with id: 5"))
                .when(service).deleteEmployee(5L);

        assertThrows(ResourceNotFoundException.class,
                () -> controller.deleteEmployee(5L));
        verify(service).deleteEmployee(5L);
    }

    // ---------------- SEARCH V2 ----------------
    
    @Test
    void searchEmployeeV2_success_allParameters() {
        EmployeeResponseV2DTO resp = new EmployeeResponseV2DTO();
        resp.setId(1L);
        resp.setFirstName("Search");
        resp.setLastName("User");
        resp.setDepartment("IT");
        resp.setSalary(5000);

        when(service.searchEmployee(1L, "Search", "User")).thenReturn(resp);

        ResponseEntity<EmployeeResponseV2DTO> result = controller.searchEmployee(1L, "Search", "User");

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1L, result.getBody().getId());
        assertEquals("Search", result.getBody().getFirstName());
        assertEquals("User", result.getBody().getLastName());
        assertEquals("IT", result.getBody().getDepartment());
        assertEquals(5000, result.getBody().getSalary());
        assertEquals("Search User", result.getBody().getFullName());
        verify(service).searchEmployee(1L, "Search", "User");
        verifyNoMoreInteractions(service);
    }

    @Test
    void searchEmployeeV2_withNullParameters() {
        EmployeeResponseV2DTO resp = new EmployeeResponseV2DTO();
        resp.setId(1L);
        resp.setFirstName("John");
        resp.setLastName("Doe");

        when(service.searchEmployee(null, null, "Doe")).thenReturn(resp);

        ResponseEntity<EmployeeResponseV2DTO> result = controller.searchEmployee(null, null, "Doe");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("John", result.getBody().getFirstName());
        assertEquals("Doe", result.getBody().getLastName());
        verify(service).searchEmployee(null, null, "Doe");
    }

    @Test
    void searchEmployeeV2_withOnlyId() {
        EmployeeResponseV2DTO resp = new EmployeeResponseV2DTO();
        resp.setId(5L);
        resp.setFirstName("Test");
        resp.setLastName("User");

        when(service.searchEmployee(5L, null, null)).thenReturn(resp);

        ResponseEntity<EmployeeResponseV2DTO> result = controller.searchEmployee(5L, null, null);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(5L, result.getBody().getId());
        verify(service).searchEmployee(5L, null, null);
    }

    @Test
    void searchEmployeeV2_withOnlyNames() {
        EmployeeResponseV2DTO resp = new EmployeeResponseV2DTO();
        resp.setFirstName("John");
        resp.setLastName("Doe");

        when(service.searchEmployee(null, "John", "Doe")).thenReturn(resp);

        ResponseEntity<EmployeeResponseV2DTO> result = controller.searchEmployee(null, "John", "Doe");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("John Doe", result.getBody().getFullName());
        verify(service).searchEmployee(null, "John", "Doe");
    }

    @Test
    void searchEmployeeV2_notFound() {
        when(service.searchEmployee(99L, "NonExistent", "User"))
                .thenThrow(new ResourceNotFoundException("No employee found with given criteria"));

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> controller.searchEmployee(99L, "NonExistent", "User")
        );

        assertTrue(exception.getMessage().contains("No employee found with given criteria"));
        verify(service).searchEmployee(99L, "NonExistent", "User");
    }

    @Test
    void searchEmployeeV2_multipleFound() {
        when(service.searchEmployee(null, "John", null))
                .thenThrow(new IllegalArgumentException("Multiple employees found. Please refine your search criteria."));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> controller.searchEmployee(null, "John", null)
        );

        assertTrue(exception.getMessage().contains("Multiple employees found"));
        verify(service).searchEmployee(null, "John", null);
    }

    @Test
    void searchEmployeeV2_withOnlyFirstName() {
        EmployeeResponseV2DTO resp = new EmployeeResponseV2DTO();
        resp.setFirstName("Alice");

        when(service.searchEmployee(null, "Alice", null)).thenReturn(resp);

        ResponseEntity<EmployeeResponseV2DTO> result = controller.searchEmployee(null, "Alice", null);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Alice", result.getBody().getFirstName());
        verify(service).searchEmployee(null, "Alice", null);
    }

    @Test
    void searchEmployeeV2_withOnlyLastName() {
        EmployeeResponseV2DTO resp = new EmployeeResponseV2DTO();
        resp.setLastName("Smith");

        when(service.searchEmployee(null, null, "Smith")).thenReturn(resp);

        ResponseEntity<EmployeeResponseV2DTO> result = controller.searchEmployee(null, null, "Smith");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Smith", result.getBody().getLastName());
        verify(service).searchEmployee(null, null, "Smith");
    }

    // ---------------- INTEGRATION SCENARIOS ----------------
    
    @Test
    void fullCRUDFlowV2() {
        // Create
        EmployeeRequestDTO createReq = new EmployeeRequestDTO("John", "Doe", "IT", 5000.0);
        EmployeeResponseV2DTO createResp = new EmployeeResponseV2DTO();
        createResp.setId(1L);
        createResp.setFirstName("John");
        createResp.setLastName("Doe");
        createResp.setMessage("Employee added successfully");
        when(service.saveEmployeeV2(any())).thenReturn(createResp);
        
        ResponseEntity<EmployeeResponseV2DTO> createResult = controller.addEmployee(createReq);
        assertEquals(HttpStatus.CREATED, createResult.getStatusCode());
        assertEquals("Employee added successfully", createResult.getBody().getMessage());

        // Read
        when(service.getEmployeeByIdV2(1L)).thenReturn(createResp);
        ResponseEntity<EmployeeResponseV2DTO> readResult = controller.getEmployee(1L);
        assertEquals(HttpStatus.OK, readResult.getStatusCode());

        // Update
        EmployeeRequestDTO updateReq = new EmployeeRequestDTO("John", "Updated", "IT", 6000.0);
        EmployeeResponseV2DTO updateResp = new EmployeeResponseV2DTO();
        updateResp.setId(1L);
        updateResp.setFirstName("John");
        updateResp.setLastName("Updated");
        updateResp.setMessage("Employee updated successfully");
        when(service.updateEmployeeV2(eq(1L), any())).thenReturn(updateResp);
        
        ResponseEntity<EmployeeResponseV2DTO> updateResult = controller.updateEmployee(1L, updateReq);
        assertEquals(HttpStatus.OK, updateResult.getStatusCode());
        assertEquals("Employee updated successfully", updateResult.getBody().getMessage());

        // Search
        when(service.searchEmployee(1L, null, null)).thenReturn(updateResp);
        ResponseEntity<EmployeeResponseV2DTO> searchResult = controller.searchEmployee(1L, null, null);
        assertEquals(HttpStatus.OK, searchResult.getStatusCode());

        // Delete
        doNothing().when(service).deleteEmployee(1L);
        ResponseEntity<String> deleteResult = controller.deleteEmployee(1L);
        assertEquals(HttpStatus.OK, deleteResult.getStatusCode());
        assertEquals("Employee deleted successfully", deleteResult.getBody());

        verify(service).saveEmployeeV2(any());
        verify(service).getEmployeeByIdV2(1L);
        verify(service).updateEmployeeV2(eq(1L), any());
        verify(service).searchEmployee(1L, null, null);
        verify(service).deleteEmployee(1L);
    }

    @Test
    void verifyResponseEntityStructureV2() {
        EmployeeRequestDTO req = new EmployeeRequestDTO("Test", "User", "IT", 5000.0);
        EmployeeResponseV2DTO resp = new EmployeeResponseV2DTO();
        resp.setId(1L);
        resp.setFirstName("Test");

        when(service.saveEmployeeV2(any())).thenReturn(resp);

        ResponseEntity<EmployeeResponseV2DTO> result = controller.addEmployee(req);

        assertNotNull(result);
        assertNotNull(result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getStatusCode().is2xxSuccessful());
    }

    @Test
    void verifyFullNameComputation() {
        EmployeeResponseV2DTO resp = new EmployeeResponseV2DTO();
        resp.setFirstName("First");
        resp.setLastName("Last");

        when(service.getEmployeeByIdV2(1L)).thenReturn(resp);

        ResponseEntity<EmployeeResponseV2DTO> result = controller.getEmployee(1L);

        assertEquals("First Last", result.getBody().getFullName());
    }
}