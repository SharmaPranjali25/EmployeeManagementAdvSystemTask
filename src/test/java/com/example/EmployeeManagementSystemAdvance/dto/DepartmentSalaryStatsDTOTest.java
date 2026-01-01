package com.example.EmployeeManagementSystemAdvance.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DepartmentSalaryStatsDTOTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        DepartmentSalaryStatsDTO dto = new DepartmentSalaryStatsDTO(
                "IT", 10, 50000, 40000, 60000, 500000
        );

        assertEquals("IT", dto.getDepartment());
        assertEquals(10, dto.getEmployeeCount());
        assertEquals(50000, dto.getAverageSalary());
        assertEquals(40000, dto.getMinSalary());
        assertEquals(60000, dto.getMaxSalary());
        assertEquals(500000, dto.getTotalSalary());
    }

    @Test
    void testEqualsHashCodeAndToString() {
        DepartmentSalaryStatsDTO dto1 =
                new DepartmentSalaryStatsDTO("IT", 10, 50000, 40000, 60000, 500000);
        DepartmentSalaryStatsDTO dto2 =
                new DepartmentSalaryStatsDTO("IT", 10, 50000, 40000, 60000, 500000);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotNull(dto1.toString());
    }
}
