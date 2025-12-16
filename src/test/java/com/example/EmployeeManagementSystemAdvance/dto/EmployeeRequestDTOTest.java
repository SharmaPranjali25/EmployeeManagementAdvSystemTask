package com.example.EmployeeManagementSystemAdvance.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeRequestDTOTest {

    @Test
    void testNoArgsConstructor() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        assertNotNull(dto);
    }

    @Test
    void testAllArgsConstructor() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO("John", "Doe", "IT", 50000.5);
        
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("IT", dto.getDepartment());
        assertEquals(50000.5, dto.getSalary());
    }

    @Test
    void testSettersAndGetters() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setDepartment("IT");
        dto.setSalary(50000.5);

        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("IT", dto.getDepartment());
        assertEquals(50000.5, dto.getSalary());
    }

    @Test
    void testEqualsAndHashCode() {
        EmployeeRequestDTO dto1 = new EmployeeRequestDTO("John", "Doe", "IT", 50000.0);
        EmployeeRequestDTO dto2 = new EmployeeRequestDTO("John", "Doe", "IT", 50000.0);
        EmployeeRequestDTO dto3 = new EmployeeRequestDTO("Jane", "Smith", "HR", 60000.0);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, dto3);
    }

    @Test
    void testToString() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO("John", "Doe", "IT", 50000.0);
        String str = dto.toString();
        
        assertNotNull(str);
        assertTrue(str.contains("John"));
        assertTrue(str.contains("Doe"));
        assertTrue(str.contains("IT"));
    }

    @Test
    void testWithNegativeSalary() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO("John", "Doe", "IT", -1000.0);
        assertEquals(-1000.0, dto.getSalary());
    }

    @Test
    void testWithZeroSalary() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO("John", "Doe", "IT", 0.0);
        assertEquals(0.0, dto.getSalary());
    }
}