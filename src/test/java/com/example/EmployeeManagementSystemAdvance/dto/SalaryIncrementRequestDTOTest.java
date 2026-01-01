package com.example.EmployeeManagementSystemAdvance.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class SalaryIncrementRequestDTOTest {

    @Test
    void testEqualsHashCodeAndToString() {
        SalaryIncrementRequestDTO dto1 =
                new SalaryIncrementRequestDTO(
                        1L,
                        5000.0,
                        10.0,
                        LocalDate.now(),
                        "Annual"
                );

        SalaryIncrementRequestDTO dto2 =
                new SalaryIncrementRequestDTO(
                        1L,
                        5000.0,
                        10.0,
                        LocalDate.now(),
                        "Annual"
                );

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotNull(dto1.toString());
    }
}
