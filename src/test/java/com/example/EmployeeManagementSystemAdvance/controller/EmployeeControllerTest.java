package com.example.EmployeeManagementSystemAdvance.controller;

import com.example.EmployeeManagementSystemAdvance.dto.EmployeeRequestDTO;
import com.example.EmployeeManagementSystemAdvance.dto.EmployeeResponseDTO;
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
class EmployeeControllerTest {

    @Mock
    private EmployeeService service;

    @InjectMocks
    private EmployeeController controller;

    // ---------------- ADD EMPLOYEE ----------------
    
    @Test
    void addEmployee_success() {
        EmployeeRequestDTO req = new EmployeeRequestDTO();
        req.setFirstName("John");
        req.setLastName("Doe");
        req.setDepartment("IT");
        req.setSalary(5000.0);

        EmployeeResponseDTO resp = new EmployeeResponseDTO();
        resp.setId(1L);
        resp.setFirstName("John");
        resp.setLastName("Doe");
        resp.setDepartment("IT");

        when(service.saveEmployeeDTO(any(EmployeeRequestDTO.class))).thenReturn(resp);

        ResponseEntity<EmployeeResponseDTO> result = controller.addEmployee(req);

        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1L, result.getBody().getId());
        assertEquals("John", result.getBody().getFirstName());
        assertEquals("Doe", result.getBody().getLastName());
        assertEquals("IT", result.getBody().getDepartment());
        verify(service).saveEmployeeDTO(any(EmployeeRequestDTO.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void addEmployee_withNullFields() {
        EmployeeRequestDTO req = new EmployeeRequestDTO();
        req.setFirstName(null);
        req.setLastName(null);
        req.setDepartment(null);
        req.setSalary(0.0);

        EmployeeResponseDTO resp = new EmployeeResponseDTO();
        resp.setId(1L);

        when(service.saveEmployeeDTO(any(EmployeeRequestDTO.class))).thenReturn(resp);

        ResponseEntity<EmployeeResponseDTO> result = controller.addEmployee(req);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(service).saveEmployeeDTO(any(EmployeeRequestDTO.class));
    }

    @Test
    void addEmployee_duplicate() {
        EmployeeRequestDTO req = new EmployeeRequestDTO();
        req.setFirstName("John");
        req.setLastName("Doe");
        req.setDepartment("IT");

        when(service.saveEmployeeDTO(any(EmployeeRequestDTO.class)))
                .thenThrow(new DuplicateResourceException("Duplicate data alert cannot save"));

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> controller.addEmployee(req)
        );

        assertEquals("Duplicate data alert cannot save", exception.getMessage());
        verify(service).saveEmployeeDTO(any(EmployeeRequestDTO.class));
    }

    @Test
    void addEmployee_withEmptyStrings() {
        EmployeeRequestDTO req = new EmployeeRequestDTO();
        req.setFirstName("");
        req.setLastName("");
        req.setDepartment("");

        EmployeeResponseDTO resp = new EmployeeResponseDTO();
        resp.setId(1L);

        when(service.saveEmployeeDTO(any(EmployeeRequestDTO.class))).thenReturn(resp);

        ResponseEntity<EmployeeResponseDTO> result = controller.addEmployee(req);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        verify(service).saveEmployeeDTO(any(EmployeeRequestDTO.class));
    }

    // ---------------- GET ALL ----------------
    
    @Test
    void getAllEmployees_success() {
        EmployeeResponseDTO r1 = new EmployeeResponseDTO();
        r1.setId(1L);
        r1.setFirstName("John");
        r1.setLastName("Doe");
        r1.setDepartment("IT");

        EmployeeResponseDTO r2 = new EmployeeResponseDTO();
        r2.setId(2L);
        r2.setFirstName("Alice");
        r2.setLastName("Smith");
        r2.setDepartment("HR");

        when(service.getAllEmployeesDTO()).thenReturn(List.of(r1, r2));

        ResponseEntity<List<EmployeeResponseDTO>> result = controller.getAllEmployees();

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(2, result.getBody().size());
        assertEquals("John", result.getBody().get(0).getFirstName());
        assertEquals("Doe", result.getBody().get(0).getLastName());
        assertEquals("IT", result.getBody().get(0).getDepartment());
        assertEquals("Alice", result.getBody().get(1).getFirstName());
        assertEquals("Smith", result.getBody().get(1).getLastName());
        assertEquals("HR", result.getBody().get(1).getDepartment());
        verify(service).getAllEmployeesDTO();
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAllEmployees_singleEmployee() {
        EmployeeResponseDTO resp = new EmployeeResponseDTO();
        resp.setId(1L);
        resp.setFirstName("John");
        resp.setLastName("Doe");

        when(service.getAllEmployeesDTO()).thenReturn(Collections.singletonList(resp));

        ResponseEntity<List<EmployeeResponseDTO>> result = controller.getAllEmployees();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        verify(service).getAllEmployeesDTO();
    }

    @Test
    void getAllEmployees_noEmployeesExist() {
        when(service.getAllEmployeesDTO())
                .thenThrow(new ResourceNotFoundException("No employee exist"));

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> controller.getAllEmployees()
        );

        assertEquals("No employee exist", exception.getMessage());
        verify(service).getAllEmployeesDTO();
    }

    @Test
    void getAllEmployees_databaseError() {
        when(service.getAllEmployeesDTO())
                .thenThrow(new RuntimeException("Unable to connect to database"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> controller.getAllEmployees()
        );

        assertTrue(exception.getMessage().contains("Unable to connect to database"));
        verify(service).getAllEmployeesDTO();
    }

    @Test
    void getAllEmployees_largeDataset() {
        EmployeeResponseDTO resp1 = new EmployeeResponseDTO(1L, "A", "Alpha", "IT");
        EmployeeResponseDTO resp2 = new EmployeeResponseDTO(2L, "B", "Beta", "HR");
        EmployeeResponseDTO resp3 = new EmployeeResponseDTO(3L, "C", "Gamma", "Finance");

        when(service.getAllEmployeesDTO()).thenReturn(List.of(resp1, resp2, resp3));

        ResponseEntity<List<EmployeeResponseDTO>> result = controller.getAllEmployees();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(3, result.getBody().size());
        verify(service).getAllEmployeesDTO();
    }

    // ---------------- GET BY ID ----------------
    
    @Test
    void getEmployeeById_found() {
        EmployeeResponseDTO resp = new EmployeeResponseDTO();
        resp.setId(1L);
        resp.setFirstName("John");
        resp.setLastName("Doe");
        resp.setDepartment("IT");

        when(service.getEmployeeByIdDTO(1L)).thenReturn(resp);

        ResponseEntity<EmployeeResponseDTO> result = controller.getEmployeeById(1L);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1L, result.getBody().getId());
        assertEquals("John", result.getBody().getFirstName());
        assertEquals("Doe", result.getBody().getLastName());
        assertEquals("IT", result.getBody().getDepartment());
        verify(service).getEmployeeByIdDTO(1L);
        verifyNoMoreInteractions(service);
    }

    @Test
    void getEmployeeById_notFound() {
        when(service.getEmployeeByIdDTO(99L))
                .thenThrow(new ResourceNotFoundException("Employee not found with id: 99"));

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> controller.getEmployeeById(99L)
        );

        assertTrue(exception.getMessage().contains("Employee not found with id: 99"));
        verify(service).getEmployeeByIdDTO(99L);
        verifyNoMoreInteractions(service);
    }

    @Test
    void getEmployeeById_withZeroId() {
        when(service.getEmployeeByIdDTO(0L))
                .thenThrow(new ResourceNotFoundException("Employee not found with id: 0"));

        assertThrows(ResourceNotFoundException.class,
                () -> controller.getEmployeeById(0L));
        verify(service).getEmployeeByIdDTO(0L);
    }

    @Test
    void getEmployeeById_withNegativeId() {
        when(service.getEmployeeByIdDTO(-1L))
                .thenThrow(new ResourceNotFoundException("Employee not found with id: -1"));

        assertThrows(ResourceNotFoundException.class,
                () -> controller.getEmployeeById(-1L));
        verify(service).getEmployeeByIdDTO(-1L);
    }

    @Test
    void getEmployeeById_withLargeId() {
        EmployeeResponseDTO resp = new EmployeeResponseDTO();
        resp.setId(999999L);
        resp.setFirstName("Test");
        resp.setLastName("User");

        when(service.getEmployeeByIdDTO(999999L)).thenReturn(resp);

        ResponseEntity<EmployeeResponseDTO> result = controller.getEmployeeById(999999L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(999999L, result.getBody().getId());
        verify(service).getEmployeeByIdDTO(999999L);
    }

    // ---------------- UPDATE ----------------
    
    @Test
    void updateEmployee_success() {
        EmployeeRequestDTO req = new EmployeeRequestDTO();
        req.setFirstName("Updated");
        req.setLastName("User");
        req.setDepartment("HR");
        req.setSalary(6000.0);

        EmployeeResponseDTO resp = new EmployeeResponseDTO();
        resp.setId(1L);
        resp.setFirstName("Updated");
        resp.setLastName("User");
        resp.setDepartment("HR");

        when(service.updateEmployeeDTO(eq(1L), any(EmployeeRequestDTO.class))).thenReturn(resp);

        ResponseEntity<EmployeeResponseDTO> result = controller.updateEmployee(1L, req);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1L, result.getBody().getId());
        assertEquals("Updated", result.getBody().getFirstName());
        assertEquals("User", result.getBody().getLastName());
        assertEquals("HR", result.getBody().getDepartment());
        verify(service).updateEmployeeDTO(eq(1L), any(EmployeeRequestDTO.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void updateEmployee_notFound() {
        EmployeeRequestDTO req = new EmployeeRequestDTO();
        req.setFirstName("Updated");
        req.setLastName("User");

        when(service.updateEmployeeDTO(eq(99L), any(EmployeeRequestDTO.class)))
                .thenThrow(new ResourceNotFoundException("Employee not found with id: 99"));

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> controller.updateEmployee(99L, req)
        );

        assertTrue(exception.getMessage().contains("Employee not found with id: 99"));
        verify(service).updateEmployeeDTO(eq(99L), any(EmployeeRequestDTO.class));
    }

    @Test
    void updateEmployee_duplicate() {
        EmployeeRequestDTO req = new EmployeeRequestDTO();
        req.setFirstName("John");
        req.setLastName("Doe");
        req.setDepartment("IT");

        when(service.updateEmployeeDTO(eq(1L), any(EmployeeRequestDTO.class)))
                .thenThrow(new DuplicateResourceException("Duplicate data alert cannot save"));

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> controller.updateEmployee(1L, req)
        );

        assertEquals("Duplicate data alert cannot save", exception.getMessage());
        verify(service).updateEmployeeDTO(eq(1L), any(EmployeeRequestDTO.class));
    }

    @Test
    void updateEmployee_withNullFields() {
        EmployeeRequestDTO req = new EmployeeRequestDTO();
        req.setFirstName(null);
        req.setLastName(null);

        EmployeeResponseDTO resp = new EmployeeResponseDTO();
        resp.setId(1L);

        when(service.updateEmployeeDTO(eq(1L), any(EmployeeRequestDTO.class))).thenReturn(resp);

        ResponseEntity<EmployeeResponseDTO> result = controller.updateEmployee(1L, req);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(service).updateEmployeeDTO(eq(1L), any(EmployeeRequestDTO.class));
    }

    @Test
    void updateEmployee_partialUpdate() {
        EmployeeRequestDTO req = new EmployeeRequestDTO();
        req.setFirstName("UpdatedName");
        req.setLastName("Doe");
        req.setDepartment("IT");
        req.setSalary(5000.0);

        EmployeeResponseDTO resp = new EmployeeResponseDTO();
        resp.setId(1L);
        resp.setFirstName("UpdatedName");
        resp.setLastName("Doe");
        resp.setDepartment("IT");

        when(service.updateEmployeeDTO(eq(1L), any(EmployeeRequestDTO.class))).thenReturn(resp);

        ResponseEntity<EmployeeResponseDTO> result = controller.updateEmployee(1L, req);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("UpdatedName", result.getBody().getFirstName());
        verify(service).updateEmployeeDTO(eq(1L), any(EmployeeRequestDTO.class));
    }

    // ---------------- DELETE ----------------
    
    @Test
    void deleteEmployee_success() {
        doNothing().when(service).deleteEmployee(1L);

        ResponseEntity<String> result = controller.deleteEmployee(1L);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Employee deleted successfully!", result.getBody());
        verify(service).deleteEmployee(1L);
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteEmployee_notFound() {
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
    void deleteEmployee_withZeroId() {
        doThrow(new ResourceNotFoundException("Employee not found with id: 0"))
                .when(service).deleteEmployee(0L);

        assertThrows(ResourceNotFoundException.class,
                () -> controller.deleteEmployee(0L));
        verify(service).deleteEmployee(0L);
    }

    @Test
    void deleteEmployee_alreadyDeleted() {
        doThrow(new ResourceNotFoundException("Employee not found with id: 5"))
                .when(service).deleteEmployee(5L);

        assertThrows(ResourceNotFoundException.class,
                () -> controller.deleteEmployee(5L));
        verify(service).deleteEmployee(5L);
    }

    @Test
    void deleteEmployee_multipleCallsSameId() {
        doNothing().when(service).deleteEmployee(1L);

        ResponseEntity<String> result1 = controller.deleteEmployee(1L);
        
        doThrow(new ResourceNotFoundException("Employee not found with id: 1"))
                .when(service).deleteEmployee(1L);

        assertEquals(HttpStatus.OK, result1.getStatusCode());
        assertThrows(ResourceNotFoundException.class,
                () -> controller.deleteEmployee(1L));
        verify(service, times(2)).deleteEmployee(1L);
    }

    // ---------------- INTEGRATION SCENARIOS ----------------
    
    @Test
    void fullCRUDFlow() {
        // Create
        EmployeeRequestDTO createReq = new EmployeeRequestDTO("John", "Doe", "IT", 5000.0);
        EmployeeResponseDTO createResp = new EmployeeResponseDTO(1L, "John", "Doe", "IT");
        when(service.saveEmployeeDTO(any())).thenReturn(createResp);
        
        ResponseEntity<EmployeeResponseDTO> createResult = controller.addEmployee(createReq);
        assertEquals(HttpStatus.CREATED, createResult.getStatusCode());

        // Read
        when(service.getEmployeeByIdDTO(1L)).thenReturn(createResp);
        ResponseEntity<EmployeeResponseDTO> readResult = controller.getEmployeeById(1L);
        assertEquals(HttpStatus.OK, readResult.getStatusCode());

        // Update
        EmployeeRequestDTO updateReq = new EmployeeRequestDTO("John", "Updated", "IT", 6000.0);
        EmployeeResponseDTO updateResp = new EmployeeResponseDTO(1L, "John", "Updated", "IT");
        when(service.updateEmployeeDTO(eq(1L), any())).thenReturn(updateResp);
        
        ResponseEntity<EmployeeResponseDTO> updateResult = controller.updateEmployee(1L, updateReq);
        assertEquals(HttpStatus.OK, updateResult.getStatusCode());

        // Delete
        doNothing().when(service).deleteEmployee(1L);
        ResponseEntity<String> deleteResult = controller.deleteEmployee(1L);
        assertEquals(HttpStatus.OK, deleteResult.getStatusCode());

        verify(service).saveEmployeeDTO(any());
        verify(service).getEmployeeByIdDTO(1L);
        verify(service).updateEmployeeDTO(eq(1L), any());
        verify(service).deleteEmployee(1L);
    }

    @Test
    void verifyResponseEntityStructure() {
        EmployeeRequestDTO req = new EmployeeRequestDTO("Test", "User", "IT", 5000.0);
        EmployeeResponseDTO resp = new EmployeeResponseDTO(1L, "Test", "User", "IT");

        when(service.saveEmployeeDTO(any())).thenReturn(resp);

        ResponseEntity<EmployeeResponseDTO> result = controller.addEmployee(req);

        assertNotNull(result);
        assertNotNull(result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getStatusCode().is2xxSuccessful());
    }
}