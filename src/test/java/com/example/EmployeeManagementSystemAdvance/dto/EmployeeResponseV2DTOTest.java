package com.example.EmployeeManagementSystemAdvance.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class EmployeeResponseV2DTOTest {

    @Test
    void testNoArgsConstructor() {
        EmployeeResponseV2DTO dto = new EmployeeResponseV2DTO();
        assertNotNull(dto);
    }

    @Test
    void testAllArgsConstructor() {
        EmployeeResponseV2DTO dto = new EmployeeResponseV2DTO(1L, "John", "Doe", "Engineering", 50000, "Employee saved successfully");
        
        assertEquals(1L, dto.getId());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("Engineering", dto.getDepartment());
        assertEquals(50000, dto.getSalary());
        assertEquals("Employee saved successfully", dto.getMessage());
    }

    @Test
    void testSettersAndGetters() {
        EmployeeResponseV2DTO dto = new EmployeeResponseV2DTO();
        
        dto.setId(2L);
        dto.setFirstName("Jane");
        dto.setLastName("Smith");
        dto.setDepartment("HR");
        dto.setSalary(60000);
        dto.setMessage("Employee updated successfully");

        assertEquals(2L, dto.getId());
        assertEquals("Jane", dto.getFirstName());
        assertEquals("Smith", dto.getLastName());
        assertEquals("HR", dto.getDepartment());
        assertEquals(60000, dto.getSalary());
        assertEquals("Employee updated successfully", dto.getMessage());
    }

    @Test
    void testGetFullName() {
        EmployeeResponseV2DTO dto = new EmployeeResponseV2DTO();

        dto.setFirstName("John");
        dto.setLastName("Doe");
        assertEquals("John Doe", dto.getFullName());

        dto.setFirstName(null);
        dto.setLastName("Doe");
        assertEquals("Doe", dto.getFullName());

        dto.setFirstName("John");
        dto.setLastName(null);
        assertEquals("John", dto.getFullName());

        dto.setFirstName(null);
        dto.setLastName(null);
        assertEquals("", dto.getFullName());
    }

    @Test
    void testEqualsAndHashCode() {
        EmployeeResponseV2DTO dto1 = new EmployeeResponseV2DTO(1L, "John", "Doe", "Engineering", 50000, "Success");
        EmployeeResponseV2DTO dto2 = new EmployeeResponseV2DTO(1L, "John", "Doe", "Engineering", 50000, "Success");
        EmployeeResponseV2DTO dto3 = new EmployeeResponseV2DTO(2L, "Jane", "Smith", "HR", 60000, "Updated");

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1, null);
        assertNotEquals(dto1, new Object());

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        EmployeeResponseV2DTO dto = new EmployeeResponseV2DTO(1L, "John", "Doe", "Engineering", 50000, "Success");
        String str = dto.toString();
        
        assertNotNull(str);
        assertTrue(str.contains("John"));
        assertTrue(str.contains("Doe"));
        assertTrue(str.contains("Engineering"));
        assertTrue(str.contains("50000"));
        assertTrue(str.contains("Success"));
    }
}