package com.example.EmployeeManagementSystemAdvance.mapper;

import com.example.EmployeeManagementSystemAdvance.dto.EmployeeRequestDTO;
import com.example.EmployeeManagementSystemAdvance.dto.EmployeeResponseDTO;
import com.example.EmployeeManagementSystemAdvance.dto.EmployeeResponseV2DTO;
import com.example.EmployeeManagementSystemAdvance.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeMapperTest {

    private EmployeeMapperImpl mapper;
    private Employee employee;
    private EmployeeRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        mapper = new EmployeeMapperImpl();

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
    }

    @Test
    void toEntity_Success() {
        Employee result = mapper.toEntity(requestDTO);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("IT", result.getDepartment());
        assertEquals(50000.0, result.getSalary());
    }

    @Test
    void toEntity_NullInput() {
        Employee result = mapper.toEntity(null);
        assertNull(result);
    }
    
    @Test
    void toEntity_NullSalary() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setFirstName("Jane");
        dto.setLastName("Smith");
        dto.setDepartment("HR");
        dto.setSalary(null);
        
        Employee result = mapper.toEntity(dto);
        
        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("HR", result.getDepartment());
        assertNull(result.getSalary());
    }

    @Test
    void toResponseDTO_Success() {
        EmployeeResponseDTO result = mapper.toResponseDTO(employee);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("IT", result.getDepartment());
    }

    @Test
    void toResponseDTO_NullInput() {
        EmployeeResponseDTO result = mapper.toResponseDTO(null);
        assertNull(result);
    }

    @Test
    void toResponseV2DTO_Success() {
        EmployeeResponseV2DTO result = mapper.toResponseV2DTO(employee);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("IT", result.getDepartment());
        assertEquals(50000, result.getSalary()); // int, not Double
    }

    @Test
    void toResponseV2DTO_NullInput() {
        EmployeeResponseV2DTO result = mapper.toResponseV2DTO(null);
        assertNull(result);
    }
    
    @Test
    void toResponseV2DTO_NullSalary() {
        Employee emp = new Employee();
        emp.setId(2L);
        emp.setFirstName("Jane");
        emp.setLastName("Smith");
        emp.setDepartment("HR");
        emp.setSalary(null);
        
        EmployeeResponseV2DTO result = mapper.toResponseV2DTO(emp);
        
        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("HR", result.getDepartment());
        // Mapper converts null to 0 for int type
        assertEquals(0, result.getSalary());
    }
    
    @Test
    void toResponseV2DTO_ZeroSalary() {
        Employee emp = new Employee();
        emp.setId(3L);
        emp.setFirstName("Bob");
        emp.setLastName("Johnson");
        emp.setDepartment("Finance");
        emp.setSalary(0.0);
        
        EmployeeResponseV2DTO result = mapper.toResponseV2DTO(emp);
        
        assertNotNull(result);
        assertEquals(0, result.getSalary());
    }
    
    @Test
    void toResponseV2DTO_NegativeSalary() {
        Employee emp = new Employee();
        emp.setId(4L);
        emp.setFirstName("Alice");
        emp.setLastName("Brown");
        emp.setDepartment("Sales");
        emp.setSalary(-1000.0);
        
        EmployeeResponseV2DTO result = mapper.toResponseV2DTO(emp);
        
        assertNotNull(result);
        assertEquals(-1000, result.getSalary()); // Converted to int
    }
    
    @Test
    void toResponseV2DTO_LargeSalary() {
        Employee emp = new Employee();
        emp.setId(5L);
        emp.setFirstName("Charlie");
        emp.setLastName("Wilson");
        emp.setDepartment("Executive");
        emp.setSalary(999999.99);
        
        EmployeeResponseV2DTO result = mapper.toResponseV2DTO(emp);
        
        assertNotNull(result);
        // Mapper converts Double to int, losing decimal precision
        assertEquals(999999, result.getSalary()); // 999999.99 -> 999999
    }
    
    @Test
    void toEntity_AllFieldsNull() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        
        Employee result = mapper.toEntity(dto);
        
        assertNotNull(result);
        assertNull(result.getFirstName());
        assertNull(result.getLastName());
        assertNull(result.getDepartment());
        assertNull(result.getSalary());
    }
    
    @Test
    void toResponseDTO_WithNullFields() {
        Employee emp = new Employee();
        emp.setId(1L);
        
        EmployeeResponseDTO result = mapper.toResponseDTO(emp);
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertNull(result.getFirstName());
        assertNull(result.getLastName());
        assertNull(result.getDepartment());
    }
    
    @Test
    void toEntity_NegativeSalary() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setFirstName("Test");
        dto.setLastName("User");
        dto.setDepartment("Testing");
        dto.setSalary(-5000.0);
        
        Employee result = mapper.toEntity(dto);
        
        assertNotNull(result);
        assertEquals(-5000.0, result.getSalary());
    }
}