package com.example.EmployeeManagementSystemAdvance.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class SalaryResponseDTOTest {

    @Test
    void testAllArgsConstructor() {
        LocalDate date = LocalDate.of(2024,1,1);
        LocalDateTime now = LocalDateTime.now();

        SalaryResponseDTO dto = new SalaryResponseDTO(
                1L, 2L, "Jane", "HR", 60000.0,
                date, null, "INCREMENT", "Annual raise", true,
                now, now, "APPROVED"
        );

        assertEquals(1L, dto.getId());
        assertEquals(2L, dto.getEmployeeId());
        assertEquals("Jane", dto.getEmployeeName());
        assertEquals("HR", dto.getDepartment());
        assertEquals(60000.0, dto.getAmount());
        assertEquals(date, dto.getEffectiveDate());
        assertNull(dto.getEndDate());
        assertEquals("INCREMENT", dto.getSalaryType());
        assertEquals("Annual raise", dto.getReason());
        assertTrue(dto.isCurrent());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now, dto.getUpdatedAt());
        assertEquals("APPROVED", dto.getMessage());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        SalaryResponseDTO dto = new SalaryResponseDTO();
        LocalDate date = LocalDate.of(2024,3,1);
        LocalDateTime createdAt = LocalDateTime.of(2024,3,1,10,0);
        LocalDateTime updatedAt = LocalDateTime.of(2024,3,2,10,0);

        dto.setId(5L);
        dto.setEmployeeId(10L);
        dto.setEmployeeName("Alice");
        dto.setDepartment("Finance");
        dto.setAmount(75000.0);
        dto.setEffectiveDate(date);
        dto.setEndDate(LocalDate.of(2024,12,31));
        dto.setSalaryType("ADJUSTMENT");
        dto.setReason("Market correction");
        dto.setCurrent(false);
        dto.setCreatedAt(createdAt);
        dto.setUpdatedAt(updatedAt);
        dto.setMessage("PENDING");

        assertEquals(5L, dto.getId());
        assertEquals(10L, dto.getEmployeeId());
        assertEquals("Alice", dto.getEmployeeName());
        assertEquals("Finance", dto.getDepartment());
        assertEquals(75000.0, dto.getAmount());
        assertEquals(date, dto.getEffectiveDate());
        assertEquals(LocalDate.of(2024,12,31), dto.getEndDate());
        assertEquals("ADJUSTMENT", dto.getSalaryType());
        assertEquals("Market correction", dto.getReason());
        assertFalse(dto.isCurrent());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(updatedAt, dto.getUpdatedAt());
        assertEquals("PENDING", dto.getMessage());
    }

    @Test
    void testEqualsHashCodeAndToString() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate date = LocalDate.now();
        SalaryResponseDTO dto1 = new SalaryResponseDTO(1L,1L,"John","IT",50000.0,date,null,"BASE","Init",true,now,now,"OK");
        SalaryResponseDTO dto2 = new SalaryResponseDTO(1L,1L,"John","IT",50000.0,date,null,"BASE","Init",true,now,now,"OK");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotNull(dto1.toString());
    }
}
