package com.example.EmployeeManagementSystemAdvance.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class SalaryDetailDTOTest {

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        SalaryDetailDTO dto = new SalaryDetailDTO(
                1L, 50000.0, LocalDate.now(), null,
                "BASE_SALARY", "Initial", true,
                now, now
        );

        assertEquals(1L, dto.getSalaryId());
        assertEquals(50000.0, dto.getAmount());
        assertNotNull(dto.getEffectiveDate());
        assertNull(dto.getEndDate());
        assertEquals("BASE_SALARY", dto.getSalaryType());
        assertEquals("Initial", dto.getReason());
        assertTrue(dto.isCurrent());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now, dto.getUpdatedAt());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        SalaryDetailDTO dto = new SalaryDetailDTO();
        LocalDateTime createdAt = LocalDateTime.of(2024,1,1,10,0);
        LocalDateTime updatedAt = LocalDateTime.of(2024,1,2,10,0);

        dto.setSalaryId(2L);
        dto.setAmount(60000.0);
        dto.setEffectiveDate(LocalDate.of(2024,1,1));
        dto.setEndDate(LocalDate.of(2024,12,31));
        dto.setSalaryType("INCREMENT");
        dto.setReason("Annual raise");
        dto.setCurrent(true);
        dto.setCreatedAt(createdAt);
        dto.setUpdatedAt(updatedAt);

        assertEquals(2L, dto.getSalaryId());
        assertEquals(60000.0, dto.getAmount());
        assertEquals(LocalDate.of(2024,1,1), dto.getEffectiveDate());
        assertEquals(LocalDate.of(2024,12,31), dto.getEndDate());
        assertEquals("INCREMENT", dto.getSalaryType());
        assertEquals("Annual raise", dto.getReason());
        assertTrue(dto.isCurrent());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(updatedAt, dto.getUpdatedAt());
    }

    @Test
    void testEqualsHashCodeAndToString() {
        LocalDateTime now = LocalDateTime.now();
        SalaryDetailDTO dto1 = new SalaryDetailDTO(1L, 50000.0, null, null, "BASE", "R", true, now, now);
        SalaryDetailDTO dto2 = new SalaryDetailDTO(1L, 50000.0, null, null, "BASE", "R", true, now, now);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotNull(dto1.toString());
    }

    @Test
    void testNullFields() {
        SalaryDetailDTO dto = new SalaryDetailDTO();
        assertNull(dto.getSalaryId());
        assertNull(dto.getAmount());
        assertNull(dto.getEffectiveDate());
        assertNull(dto.getEndDate());
        assertNull(dto.getSalaryType());
        assertNull(dto.getReason());
        assertFalse(dto.isCurrent());
        assertNull(dto.getCreatedAt());
        assertNull(dto.getUpdatedAt());
    }
}
