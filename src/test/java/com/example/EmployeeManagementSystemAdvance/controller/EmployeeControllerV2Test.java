package com.example.EmployeeManagementSystemAdvance.controller;

import com.example.EmployeeManagementSystemAdvance.dto.EmployeeRequestDTO;
import com.example.EmployeeManagementSystemAdvance.dto.EmployeeResponseV2DTO;
import com.example.EmployeeManagementSystemAdvance.exception.DuplicateResourceException;
import com.example.EmployeeManagementSystemAdvance.exception.ResourceNotFoundException;
import com.example.EmployeeManagementSystemAdvance.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

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

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(controller, "appTitle", "EMS App");
        ReflectionTestUtils.setField(controller, "appDescription", "Employee Management System");
    }

    // ---------------- ADD EMPLOYEE ----------------

    @Test
    void addEmployee_success() {
        EmployeeRequestDTO req = new EmployeeRequestDTO("Armaan", "Khan", "IT", 5000.0);

        EmployeeResponseV2DTO resp = new EmployeeResponseV2DTO();
        resp.setId(1L);
        resp.setFirstName("Armaan");
        resp.setLastName("Khan");
        resp.setDepartment("IT");
        resp.setSalary(5000);
        resp.setMessage("Employee added successfully");

        when(service.saveEmployeeV2(any())).thenReturn(resp);

        ResponseEntity<EmployeeResponseV2DTO> result = controller.addEmployee(req);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("Employee added successfully", result.getBody().getMessage());
        verify(service).saveEmployeeV2(any());
    }

    @Test
    void addEmployee_duplicate() {
        when(service.saveEmployeeV2(any()))
                .thenThrow(new DuplicateResourceException("Duplicate data alert cannot save"));

        assertThrows(DuplicateResourceException.class,
                () -> controller.addEmployee(new EmployeeRequestDTO()));
    }

    // ---------------- GET ALL ----------------

    @Test
    void getAllEmployees_success() {
        when(service.getAllEmployeesV2()).thenReturn(List.of(new EmployeeResponseV2DTO()));

        ResponseEntity<List<EmployeeResponseV2DTO>> result = controller.getAllEmployees();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(service).getAllEmployeesV2();
    }

    @Test
    void getAllEmployees_notFound() {
        when(service.getAllEmployeesV2())
                .thenThrow(new ResourceNotFoundException("No employee exist"));

        assertThrows(ResourceNotFoundException.class,
                () -> controller.getAllEmployees());
    }

    // ---------------- GET BY ID ----------------

    @Test
    void getEmployee_found() {
        EmployeeResponseV2DTO resp = new EmployeeResponseV2DTO();
        resp.setFirstName("John");
        resp.setLastName("Doe");

        when(service.getEmployeeByIdV2(1L)).thenReturn(resp);

        ResponseEntity<EmployeeResponseV2DTO> result = controller.getEmployee(1L);

        assertEquals("John Doe", result.getBody().getFullName());
        verify(service).getEmployeeByIdV2(1L);
    }

    // ---------------- UPDATE ----------------

    @Test
    void updateEmployee_success() {
        EmployeeResponseV2DTO resp = new EmployeeResponseV2DTO();
        resp.setMessage("Employee updated successfully");

        when(service.updateEmployeeV2(eq(1L), any())).thenReturn(resp);

        ResponseEntity<EmployeeResponseV2DTO> result =
                controller.updateEmployee(1L, new EmployeeRequestDTO());

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Employee updated successfully", result.getBody().getMessage());
    }

    // ---------------- DELETE ----------------

    @Test
    void deleteEmployee_success() {
        doNothing().when(service).deleteEmployee(1L);

        ResponseEntity<String> result = controller.deleteEmployee(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Employee deleted successfully", result.getBody());
    }

    // ---------------- SEARCH ----------------

    @Test
    void searchEmployee_success() {
        EmployeeResponseV2DTO resp = new EmployeeResponseV2DTO();
        resp.setFirstName("Search");
        resp.setLastName("User");

        when(service.searchEmployee(null, "Search", "User")).thenReturn(resp);

        ResponseEntity<EmployeeResponseV2DTO> result =
                controller.searchEmployee(null, "Search", "User");

        assertEquals("Search User", result.getBody().getFullName());
    }
}
