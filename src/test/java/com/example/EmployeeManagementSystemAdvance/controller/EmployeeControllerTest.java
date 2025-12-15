//package com.example.EmployeeManagementSystem.repository;
//import com.example.EmployeeManagementSystem.controller.EmployeeController;
//import com.example.EmployeeManagementSystem.dto.EmployeeRequestDTO;
//import com.example.EmployeeManagementSystem.dto.EmployeeResponseDTO;
//import com.example.EmployeeManagementSystem.entity.Employee;
//import com.example.EmployeeManagementSystem.exception.ResourceNotFoundException;
//import com.example.EmployeeManagementSystem.service.EmployeeService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import static org.mockito.Mockito.when;
//import static org.mockito.Mockito.any;
//import static org.mockito.Mockito.doNothing;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import java.util.Arrays;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(EmployeeController.class)
//class EmployeeControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private EmployeeService employeeService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    // ---------------- POST /add ----------------
//    @Test
//    void testAddEmployee() throws Exception {
//        EmployeeRequestDTO requestDTO = new EmployeeRequestDTO();
//        requestDTO.setName("John");
//        requestDTO.setDepartment("IT");
//        requestDTO.setSalary(5000);
//
//        // Mock service to return Employee entity
//        when(employeeService.saveEmployeeEntity(any()))
//                .thenReturn(new Employee(1L, "John", "IT", 5000));
//
//        mockMvc.perform(post("/api/v1/employees/add")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.name").value("John"))
//                .andExpect(jsonPath("$.department").value("IT"));
//
//        verify(employeeService).saveEmployeeEntity(any());
//    }
//
//    // ---------------- GET /all ----------------
//    @Test
//    void testGetAllEmployees() throws Exception {
//        // Mock service to return DTOs (matches controller expectation)
//        when(employeeService.getAllEmployees()).thenReturn(
//                Arrays.asList(
//                        new EmployeeResponseDTO(1L, "John", "IT"),
//                        new EmployeeResponseDTO(2L, "Alice", "HR")
//                )
//        );
//
//        mockMvc.perform(get("/api/v1/employees/all"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].name").value("John"))
//                .andExpect(jsonPath("$[1].name").value("Alice"));
//
//        verify(employeeService).getAllEmployees();
//    }
//
//    // ---------------- GET /{id} ----------------
//    @Test
//    void testGetEmployeeById_found() throws Exception {
//        long id = 1L;
//        when(employeeService.getEmployeeById(id))
//                .thenReturn(Optional.of(new Employee(id, "John", "IT", 5000)));
//
//        mockMvc.perform(get("/api/v1/employees/{id}", id))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("John"))
//                .andExpect(jsonPath("$.department").value("IT"));
//
//        verify(employeeService).getEmployeeById(id);
//    }
//
//    @Test
//    void testGetEmployeeById_notFound() throws Exception {
//        long id = 99L;
//        when(employeeService.getEmployeeById(id)).thenReturn(Optional.empty());
//
//        mockMvc.perform(get("/api/v1/employees/{id}", id))
//                .andExpect(status().isNotFound());
//
//        verify(employeeService).getEmployeeById(id);
//    }
//
//    // ---------------- PUT /update/{id} ----------------
//    @Test
//    void testUpdateEmployee() throws Exception {
//        long id = 1L;
//        EmployeeRequestDTO requestDTO = new EmployeeRequestDTO();
//        requestDTO.setName("John Updated");
//        requestDTO.setDepartment("IT Updated");
//        requestDTO.setSalary(6000);
//
//        when(employeeService.updateEmployee(eq(id), any()))
//                .thenReturn(new Employee(id, "John Updated", "IT Updated", 6000));
//
//        mockMvc.perform(put("/api/v1/employees/update/{id}", id)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("John Updated"))
//                .andExpect(jsonPath("$.department").value("IT Updated"));
//
//        verify(employeeService).updateEmployee(eq(id), any());
//    }
//
//    @Test
//    void testUpdateEmployee_notFound() throws Exception {
//        long id = 99L;
//        EmployeeRequestDTO requestDTO = new EmployeeRequestDTO();
//        requestDTO.setName("Not Found");
//        requestDTO.setDepartment("NA");
//        requestDTO.setSalary(0);
//
//        when(employeeService.updateEmployee(eq(id), any()))
//                .thenThrow(new ResourceNotFoundException("Employee not found with id: " + id));
//
//        mockMvc.perform(put("/api/v1/employees/update/{id}", id)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDTO)))
//                .andExpect(status().isNotFound());
//
//        verify(employeeService).updateEmployee(eq(id), any());
//    }
//
//    // ---------------- DELETE /delete/{id} ----------------
//    @Test
//    void testDeleteEmployee() throws Exception {
//        long id = 1L;
//        doNothing().when(employeeService).deleteEmployee(id);
//
//        mockMvc.perform(delete("/api/v1/employees/delete/{id}", id))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Employee deleted successfully!"));
//
//        verify(employeeService).deleteEmployee(id);
//    }
//
//    @Test
//    void testDeleteEmployee_notFound() throws Exception {
//        long id = 99L;
//        doThrow(new ResourceNotFoundException("Employee not found with id: " + id))
//                .when(employeeService).deleteEmployee(id);
//
//        mockMvc.perform(delete("/api/v1/employees/delete/{id}", id))
//                .andExpect(status().isNotFound());
//
//        verify(employeeService).deleteEmployee(id);
//    }
//}
//injectmock-controller methods 
package com.example.EmployeeManagementSystemAdvance.controller;
import com.example.EmployeeManagementSystemAdvance.dto.EmployeeRequestDTO;
import com.example.EmployeeManagementSystemAdvance.dto.EmployeeResponseDTO;
import com.example.EmployeeManagementSystemAdvance.entity.Employee;
import com.example.EmployeeManagementSystemAdvance.exception.ResourceNotFoundException;
import com.example.EmployeeManagementSystemAdvance.service.EmployeeService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeService service;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeController controller;

    // ---------------- ADD EMPLOYEE ----------------
    @Test
    void addEmployee_success() {

        EmployeeRequestDTO request = new EmployeeRequestDTO();
        request.setName("John");
        request.setDepartment("IT");
        request.setSalary(5000);

        Employee savedEntity = new Employee(1L, "John", "IT", 5000);
        EmployeeResponseDTO responseDTO =
                new EmployeeResponseDTO(1L, "John", "IT");

        when(service.saveEmployeeEntity(any())).thenReturn(savedEntity);
        when(modelMapper.map(savedEntity, EmployeeResponseDTO.class))
                .thenReturn(responseDTO);

        ResponseEntity<EmployeeResponseDTO> response =
                controller.addEmployee(request);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("John", response.getBody().getName());

        verify(service).saveEmployeeEntity(any());
        verify(modelMapper).map(savedEntity, EmployeeResponseDTO.class);
    }

    // ---------------- GET ALL ----------------
    @Test
    void getAllEmployees_success() {

        EmployeeResponseDTO r1 = new EmployeeResponseDTO(1L, "John", "IT");
        EmployeeResponseDTO r2 = new EmployeeResponseDTO(2L, "Alice", "HR");

        when(service.getAllEmployees())
                .thenReturn(Arrays.asList(r1, r2));

        ResponseEntity<List<EmployeeResponseDTO>> response =
                controller.getAllEmployees();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("John", response.getBody().get(0).getName());

        verify(service).getAllEmployees();
    }


    // ---------------- GET BY ID ----------------
    @Test
    void getEmployeeById_found() {

        Employee emp = new Employee(1L, "John", "IT", 5000);
        EmployeeResponseDTO responseDTO =
                new EmployeeResponseDTO(1L, "John", "IT");

        when(service.getEmployeeById(1L)).thenReturn(Optional.of(emp));
        when(modelMapper.map(emp, EmployeeResponseDTO.class))
                .thenReturn(responseDTO);

        ResponseEntity<EmployeeResponseDTO> response =
                controller.getEmployeeById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("John", response.getBody().getName());

        verify(service).getEmployeeById(1L);
    }

    @Test
    void getEmployeeById_notFound() {

        when(service.getEmployeeById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> controller.getEmployeeById(99L));

        verify(service).getEmployeeById(99L);
    }

    // ---------------- UPDATE ----------------
    @Test
    void updateEmployee_success() {

        EmployeeRequestDTO request = new EmployeeRequestDTO();
        request.setName("Updated");
        request.setDepartment("UpdatedDept");

        EmployeeResponseDTO responseDTO =
                new EmployeeResponseDTO(1L, "Updated", "UpdatedDept");

        when(service.updateEmployee(eq(1L), any()))
                .thenReturn(responseDTO);

        ResponseEntity<EmployeeResponseDTO> response =
                controller.updateEmployee(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated", response.getBody().getName());

        verify(service).updateEmployee(eq(1L), any());
    }

    @Test
    void updateEmployee_notFound() {

        when(service.updateEmployee(eq(99L), any()))
                .thenThrow(new ResourceNotFoundException("Not found"));

        assertThrows(ResourceNotFoundException.class,
                () -> controller.updateEmployee(99L, new EmployeeRequestDTO()));

        verify(service).updateEmployee(eq(99L), any());
    }

    // ---------------- DELETE ----------------
    @Test
    void deleteEmployee_success() {

        doNothing().when(service).deleteEmployee(1L);

        ResponseEntity<String> response =
                controller.deleteEmployee(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Employee deleted successfully!", response.getBody());

        verify(service).deleteEmployee(1L);
    }

    @Test
    void deleteEmployee_notFound() {

        doThrow(new ResourceNotFoundException("Not found"))
                .when(service).deleteEmployee(99L);

        assertThrows(ResourceNotFoundException.class,
                () -> controller.deleteEmployee(99L));

        verify(service).deleteEmployee(99L);
    }
}


