package com.example.EmployeeManagementSystemAdvance.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class EmployeeResponseDTOTest {

    @Test
    void testNoArgsConstructor() {
        EmployeeResponseDTO dto = new EmployeeResponseDTO();
        assertNotNull(dto);
    }

    @Test
    void testAllArgsConstructor() {
        EmployeeResponseDTO dto = new EmployeeResponseDTO(1L, "John", "Doe", "Engineering");
        
        assertEquals(1L, dto.getId());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("Engineering", dto.getDepartment());
    }

    @Test
    void testSettersAndGetters() {
        EmployeeResponseDTO dto = new EmployeeResponseDTO();
        
        dto.setId(2L);
        dto.setFirstName("Jane");
        dto.setLastName("Smith");
        dto.setDepartment("HR");

        assertEquals(2L, dto.getId());
        assertEquals("Jane", dto.getFirstName());
        assertEquals("Smith", dto.getLastName());
        assertEquals("HR", dto.getDepartment());
    }

    @Test
    void testEqualsAndHashCode() {
        EmployeeResponseDTO dto1 = new EmployeeResponseDTO(1L, "John", "Doe", "Engineering");
        EmployeeResponseDTO dto2 = new EmployeeResponseDTO(1L, "John", "Doe", "Engineering");
        EmployeeResponseDTO dto3 = new EmployeeResponseDTO(2L, "Jane", "Smith", "HR");

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1, null);
        assertNotEquals(dto1, new Object());

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        EmployeeResponseDTO dto = new EmployeeResponseDTO(1L, "John", "Doe", "Engineering");
        String str = dto.toString();
        
        assertNotNull(str);
        assertTrue(str.contains("John"));
        assertTrue(str.contains("Doe"));
        assertTrue(str.contains("Engineering"));
    }
}