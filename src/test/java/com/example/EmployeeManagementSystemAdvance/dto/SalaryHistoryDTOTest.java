package com.example.EmployeeManagementSystemAdvance.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SalaryHistoryDTOTest {

    @Test
    void testEqualsHashCodeAndToString() {
        SalaryDetailDTO detail = new SalaryDetailDTO(
                1L, 50000.0, null, null, "BASE", "Init", true,
                LocalDateTime.now(), LocalDateTime.now()
        );

        SalaryHistoryDTO dto1 = new SalaryHistoryDTO(1L, "John", "IT", 50000.0, List.of(detail));
        SalaryHistoryDTO dto2 = new SalaryHistoryDTO(1L, "John", "IT", 50000.0, List.of(detail));

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotNull(dto1.toString());
    }

    @Test
    void testNoArgsConstructor() {
        SalaryHistoryDTO dto = new SalaryHistoryDTO();

        assertNull(dto.getEmployeeId());
        assertNull(dto.getEmployeeName());
        assertNull(dto.getDepartment());
        assertEquals(0.0, dto.getCurrentSalary());
        assertNull(dto.getSalaryHistory());
    }

    @Test
    void testAllArgsConstructor() {
        SalaryDetailDTO detail1 = new SalaryDetailDTO(
                1L, 50000.0, LocalDate.of(2024, 1, 1), null,
                "BASE_SALARY", "Initial", false,
                LocalDateTime.now(), LocalDateTime.now()
        );
        SalaryDetailDTO detail2 = new SalaryDetailDTO(
                2L, 55000.0, LocalDate.of(2024, 6, 1), null,
                "INCREMENT", "Annual raise", true,
                LocalDateTime.now(), LocalDateTime.now()
        );
        List<SalaryDetailDTO> history = Arrays.asList(detail1, detail2);

        SalaryHistoryDTO dto = new SalaryHistoryDTO(1L, "Jane Doe", "HR", 55000.0, history);

        assertEquals(1L, dto.getEmployeeId());
        assertEquals("Jane Doe", dto.getEmployeeName());
        assertEquals("HR", dto.getDepartment());
        assertEquals(55000.0, dto.getCurrentSalary());
        assertEquals(2, dto.getSalaryHistory().size());
    }

    @Test
    void testGettersAndSetters() {
        SalaryHistoryDTO dto = new SalaryHistoryDTO();

        SalaryDetailDTO detail = new SalaryDetailDTO(
                1L, 60000.0, LocalDate.now(), null,
                "BASE_SALARY", "Initial", true,
                LocalDateTime.now(), LocalDateTime.now()
        );
        List<SalaryDetailDTO> history = List.of(detail);

        dto.setEmployeeId(5L);
        dto.setEmployeeName("Alice Smith");
        dto.setDepartment("Finance");
        dto.setCurrentSalary(60000.0);
        dto.setSalaryHistory(history);

        assertEquals(5L, dto.getEmployeeId());
        assertEquals("Alice Smith", dto.getEmployeeName());
        assertEquals("Finance", dto.getDepartment());
        assertEquals(60000.0, dto.getCurrentSalary());
        assertEquals(1, dto.getSalaryHistory().size());
    }

    @Test
    void testEmptySalaryHistory() {
        SalaryHistoryDTO dto = new SalaryHistoryDTO(
                1L, "John", "IT", 50000.0, Collections.emptyList()
        );

        assertNotNull(dto.getSalaryHistory());
        assertTrue(dto.getSalaryHistory().isEmpty());
    }

    @Test
    void testNullSalaryHistory() {
        SalaryHistoryDTO dto = new SalaryHistoryDTO(
                1L, "John", "IT", 50000.0, null
        );

        assertNull(dto.getSalaryHistory());
    }

    @Test
    void testMultipleSalaryEntries() {
        SalaryDetailDTO detail1 = new SalaryDetailDTO(
                1L, 50000.0, LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31),
                "BASE_SALARY", "Initial", false,
                LocalDateTime.now(), LocalDateTime.now()
        );
        SalaryDetailDTO detail2 = new SalaryDetailDTO(
                2L, 55000.0, LocalDate.of(2024, 1, 1), null,
                "INCREMENT", "Annual raise", true,
                LocalDateTime.now(), LocalDateTime.now()
        );
        List<SalaryDetailDTO> history = Arrays.asList(detail1, detail2);

        SalaryHistoryDTO dto = new SalaryHistoryDTO(1L, "John", "IT", 55000.0, history);

        assertEquals(2, dto.getSalaryHistory().size());
        assertEquals(55000.0, dto.getCurrentSalary());
    }

    @Test
    void testZeroAndNegativeCurrentSalary() {
        SalaryHistoryDTO dtoZero = new SalaryHistoryDTO();
        dtoZero.setCurrentSalary(0.0);
        assertEquals(0.0, dtoZero.getCurrentSalary());

        SalaryHistoryDTO dtoNegative = new SalaryHistoryDTO();
        dtoNegative.setCurrentSalary(-5000.0);
        assertEquals(-5000.0, dtoNegative.getCurrentSalary());
    }

    @Test
    void testLargeCurrentSalary() {
        SalaryHistoryDTO dto = new SalaryHistoryDTO();
        dto.setCurrentSalary(999_999_999.99);
        assertEquals(999_999_999.99, dto.getCurrentSalary());
    }

    @Test
    void testEqualsWithDifferentValues() {
        SalaryDetailDTO detail = new SalaryDetailDTO(
                1L, 50000.0, null, null, "BASE", "Init", true,
                LocalDateTime.now(), LocalDateTime.now()
        );

        SalaryHistoryDTO dto1 = new SalaryHistoryDTO(1L, "John", "IT", 50000.0, List.of(detail));
        SalaryHistoryDTO dto2 = new SalaryHistoryDTO(2L, "John", "IT", 50000.0, List.of(detail));
        SalaryHistoryDTO dto3 = new SalaryHistoryDTO(1L, "Jane", "IT", 50000.0, List.of(detail));
        SalaryHistoryDTO dto4 = new SalaryHistoryDTO(1L, "John", "IT", 60000.0, List.of(detail));

        assertNotEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1, dto4);
    }

    @Test
    void testEqualsAndHashCodeConsistency() {
        SalaryDetailDTO detail = new SalaryDetailDTO(
                1L, 50000.0, null, null, "BASE", "Init", true,
                LocalDateTime.now(), LocalDateTime.now()
        );
        SalaryHistoryDTO dto = new SalaryHistoryDTO(1L, "John", "IT", 50000.0, List.of(detail));

        assertEquals(dto, dto);
        assertNotEquals(dto, null);
        assertNotEquals(dto, new Object());
        assertEquals(dto.hashCode(), dto.hashCode());
    }

    @Test
    void testToStringContainsAllFields() {
        SalaryDetailDTO detail = new SalaryDetailDTO(
                1L, 50000.0, null, null, "BASE", "Init", true,
                LocalDateTime.now(), LocalDateTime.now()
        );
        SalaryHistoryDTO dto = new SalaryHistoryDTO(1L, "John", "IT", 50000.0, List.of(detail));

        String toString = dto.toString();
        assertTrue(toString.contains("SalaryHistoryDTO"));
        assertTrue(toString.contains("employeeId"));
        assertTrue(toString.contains("employeeName"));
        assertTrue(toString.contains("currentSalary"));
    }

    @Test
    void testModifyingValuesAndMutableList() {
        SalaryDetailDTO detail1 = new SalaryDetailDTO(
                1L, 50000.0, null, null, "BASE", "Init", true,
                LocalDateTime.now(), LocalDateTime.now()
        );
        SalaryHistoryDTO dto = new SalaryHistoryDTO(
                1L, "John", "IT", 50000.0, new ArrayList<>(List.of(detail1))
        );

        SalaryDetailDTO detail2 = new SalaryDetailDTO(
                2L, 60000.0, null, null, "INCREMENT", "Raise", true,
                LocalDateTime.now(), LocalDateTime.now()
        );
        dto.getSalaryHistory().add(detail2);

        dto.setEmployeeId(2L);
        dto.setEmployeeName("Jane");
        dto.setDepartment("HR");
        dto.setCurrentSalary(60000.0);

        assertEquals(2L, dto.getEmployeeId());
        assertEquals("Jane", dto.getEmployeeName());
        assertEquals("HR", dto.getDepartment());
        assertEquals(60000.0, dto.getCurrentSalary());
        assertEquals(2, dto.getSalaryHistory().size());
    }

    @Test
    void testSalaryHistoryWithDifferentSalaryTypes() {
        SalaryDetailDTO detail1 = new SalaryDetailDTO(
                1L, 50000.0, LocalDate.of(2023, 1, 1), null, "BASE_SALARY", "Initial", false,
                LocalDateTime.now(), LocalDateTime.now()
        );
        SalaryDetailDTO detail2 = new SalaryDetailDTO(
                2L, 55000.0, LocalDate.of(2023, 6, 1), null, "INCREMENT", "Mid-year raise", false,
                LocalDateTime.now(), LocalDateTime.now()
        );
        SalaryDetailDTO detail3 = new SalaryDetailDTO(
                3L, 60000.0, LocalDate.of(2024, 1, 1), null, "ADJUSTMENT", "Market adjustment", true,
                LocalDateTime.now(), LocalDateTime.now()
        );
        List<SalaryDetailDTO> history = Arrays.asList(detail1, detail2, detail3);

        SalaryHistoryDTO dto = new SalaryHistoryDTO(1L, "John", "IT", 60000.0, history);

        assertEquals(3, dto.getSalaryHistory().size());
        assertEquals("BASE_SALARY", dto.getSalaryHistory().get(0).getSalaryType());
        assertEquals("INCREMENT", dto.getSalaryHistory().get(1).getSalaryType());
        assertEquals("ADJUSTMENT", dto.getSalaryHistory().get(2).getSalaryType());
    }
}
