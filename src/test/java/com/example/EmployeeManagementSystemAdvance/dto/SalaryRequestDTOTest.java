package com.example.EmployeeManagementSystemAdvance.dto;

import com.example.EmployeeManagementSystemAdvance.entity.Salary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class SalaryRequestDTOTest {

    private SalaryRequestDTO dto1;
    private SalaryRequestDTO dto2;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(2024, 1, 1);
        
        dto1 = new SalaryRequestDTO(
                1L, 
                50000.0, 
                testDate, 
                Salary.SalaryType.BASE_SALARY, 
                "Init"
        );
        
        dto2 = new SalaryRequestDTO(
                1L, 
                50000.0, 
                testDate, 
                Salary.SalaryType.BASE_SALARY, 
                "Init"
        );
    }

    @Test
    void testEqualsHashCodeAndToString() {
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotNull(dto1.toString());
        assertTrue(dto1.toString().contains("employeeId=1"));
        assertTrue(dto1.toString().contains("amount=50000.0"));
    }

    @Test
    void testNoArgsConstructor() {
        SalaryRequestDTO dto = new SalaryRequestDTO();
        
        assertNull(dto.getEmployeeId());
        assertNull(dto.getAmount());
        assertNull(dto.getEffectiveDate());
        assertNull(dto.getSalaryType());
        assertNull(dto.getReason());
    }

    @Test
    void testAllArgsConstructor() {
        SalaryRequestDTO dto = new SalaryRequestDTO(
                1L,
                75000.0,
                LocalDate.of(2024, 6, 1),
                Salary.SalaryType.INCREMENT,
                "Annual raise"
        );

        assertEquals(1L, dto.getEmployeeId());
        assertEquals(75000.0, dto.getAmount());
        assertEquals(LocalDate.of(2024, 6, 1), dto.getEffectiveDate());
        assertEquals(Salary.SalaryType.INCREMENT, dto.getSalaryType());
        assertEquals("Annual raise", dto.getReason());
    }

    @Test
    void testGettersAndSetters() {
        SalaryRequestDTO dto = new SalaryRequestDTO();
        
        dto.setEmployeeId(5L);
        dto.setAmount(60000.0);
        dto.setEffectiveDate(LocalDate.of(2024, 3, 15));
        dto.setSalaryType(Salary.SalaryType.ADJUSTMENT);
        dto.setReason("Market correction");

        assertEquals(5L, dto.getEmployeeId());
        assertEquals(60000.0, dto.getAmount());
        assertEquals(LocalDate.of(2024, 3, 15), dto.getEffectiveDate());
        assertEquals(Salary.SalaryType.ADJUSTMENT, dto.getSalaryType());
        assertEquals("Market correction", dto.getReason());
    }

    @Test
    void testNullAmount() {
        SalaryRequestDTO dto = new SalaryRequestDTO();
        dto.setEmployeeId(1L);
        dto.setAmount(null);
        dto.setEffectiveDate(LocalDate.now());
        dto.setSalaryType(Salary.SalaryType.BASE_SALARY);
        dto.setReason("Test");
        
        assertNull(dto.getAmount());
        assertNotNull(dto.toString());
    }

    @Test
    void testZeroAmount() {
        SalaryRequestDTO dto = new SalaryRequestDTO();
        dto.setEmployeeId(1L);
        dto.setAmount(0.0);
        dto.setEffectiveDate(LocalDate.now());
        
        assertEquals(0.0, dto.getAmount());
    }

    @Test
    void testNegativeAmount() {
        SalaryRequestDTO dto = new SalaryRequestDTO();
        dto.setEmployeeId(1L);
        dto.setAmount(-5000.0);
        dto.setEffectiveDate(LocalDate.now());
        
        assertEquals(-5000.0, dto.getAmount());
    }

    @Test
    void testDifferentSalaryTypes() {
        SalaryRequestDTO dto1 = new SalaryRequestDTO(
                1L, 50000.0, testDate, Salary.SalaryType.BASE_SALARY, "Base"
        );
        SalaryRequestDTO dto2 = new SalaryRequestDTO(
                1L, 50000.0, testDate, Salary.SalaryType.INCREMENT, "Increment"
        );
        SalaryRequestDTO dto3 = new SalaryRequestDTO(
                1L, 50000.0, testDate, Salary.SalaryType.ADJUSTMENT, "Adjustment"
        );
        SalaryRequestDTO dto4 = new SalaryRequestDTO(
                1L, 50000.0, testDate, Salary.SalaryType.BONUS, "Bonus"
        );
        SalaryRequestDTO dto5 = new SalaryRequestDTO(
                1L, 50000.0, testDate, Salary.SalaryType.DEDUCTION, "Deduction"
        );

        assertEquals(Salary.SalaryType.BASE_SALARY, dto1.getSalaryType());
        assertEquals(Salary.SalaryType.INCREMENT, dto2.getSalaryType());
        assertEquals(Salary.SalaryType.ADJUSTMENT, dto3.getSalaryType());
        assertEquals(Salary.SalaryType.BONUS, dto4.getSalaryType());
        assertEquals(Salary.SalaryType.DEDUCTION, dto5.getSalaryType());
    }

    @Test
    void testEqualsWithDifferentEmployeeId() {
        SalaryRequestDTO dto2 = new SalaryRequestDTO(
                2L,
                50000.0,
                testDate,
                Salary.SalaryType.BASE_SALARY,
                "Init"
        );

        assertNotEquals(dto1, dto2);
    }

    @Test
    void testEqualsWithDifferentAmount() {
        SalaryRequestDTO dto2 = new SalaryRequestDTO(
                1L,
                60000.0,
                testDate,
                Salary.SalaryType.BASE_SALARY,
                "Init"
        );

        assertNotEquals(dto1, dto2);
    }

    @Test
    void testEqualsWithDifferentDate() {
        SalaryRequestDTO dto2 = new SalaryRequestDTO(
                1L,
                50000.0,
                LocalDate.of(2024, 2, 1),
                Salary.SalaryType.BASE_SALARY,
                "Init"
        );

        assertNotEquals(dto1, dto2);
    }

    @Test
    void testEqualsWithDifferentSalaryType() {
        SalaryRequestDTO dto2 = new SalaryRequestDTO(
                1L,
                50000.0,
                testDate,
                Salary.SalaryType.INCREMENT,
                "Init"
        );

        assertNotEquals(dto1, dto2);
    }

    @Test
    void testEqualsWithDifferentReason() {
        SalaryRequestDTO dto2 = new SalaryRequestDTO(
                1L,
                50000.0,
                testDate,
                Salary.SalaryType.BASE_SALARY,
                "Different reason"
        );

        assertNotEquals(dto1, dto2);
    }

    @Test
    void testEqualsWithSameObject() {
        assertEquals(dto1, dto1);
    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(dto1, null);
    }

    @Test
    void testEqualsWithDifferentClass() {
        assertNotEquals(dto1, new Object());
    }

    @Test
    void testHashCodeConsistency() {
        int hashCode1 = dto1.hashCode();
        int hashCode2 = dto1.hashCode();
        
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testHashCodeEquality() {
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToStringContainsAllFields() {
        String toString = dto1.toString();
        
        assertTrue(toString.contains("SalaryRequestDTO"));
        assertTrue(toString.contains("employeeId"));
        assertTrue(toString.contains("amount"));
        assertTrue(toString.contains("effectiveDate"));
        assertTrue(toString.contains("salaryType"));
        assertTrue(toString.contains("reason"));
    }

    @Test
    void testNullFields() {
        SalaryRequestDTO dto = new SalaryRequestDTO(
                null,
                null,
                null,
                null,
                null
        );

        assertNull(dto.getEmployeeId());
        assertNull(dto.getAmount());
        assertNull(dto.getEffectiveDate());
        assertNull(dto.getSalaryType());
        assertNull(dto.getReason());
    }

    @Test
    void testEmptyReason() {
        SalaryRequestDTO dto = new SalaryRequestDTO(
                1L,
                50000.0,
                testDate,
                Salary.SalaryType.BASE_SALARY,
                ""
        );

        assertEquals("", dto.getReason());
    }

    @Test
    void testLargeAmount() {
        SalaryRequestDTO dto = new SalaryRequestDTO();
        dto.setAmount(999999999.99);
        
        assertEquals(999999999.99, dto.getAmount());
    }

    @Test
    void testPastDate() {
        LocalDate pastDate = LocalDate.of(2020, 1, 1);
        SalaryRequestDTO dto = new SalaryRequestDTO(
                1L,
                50000.0,
                pastDate,
                Salary.SalaryType.BASE_SALARY,
                "Past date"
        );

        assertEquals(pastDate, dto.getEffectiveDate());
        assertTrue(dto.getEffectiveDate().isBefore(LocalDate.now()));
    }

    @Test
    void testFutureDate() {
        LocalDate futureDate = LocalDate.now().plusYears(1);
        SalaryRequestDTO dto = new SalaryRequestDTO(
                1L,
                50000.0,
                futureDate,
                Salary.SalaryType.BASE_SALARY,
                "Future date"
        );

        assertEquals(futureDate, dto.getEffectiveDate());
        assertTrue(dto.getEffectiveDate().isAfter(LocalDate.now()));
    }

    @Test
    void testModifyingValues() {
        SalaryRequestDTO dto = new SalaryRequestDTO(
                1L,
                50000.0,
                testDate,
                Salary.SalaryType.BASE_SALARY,
                "Initial"
        );

        dto.setEmployeeId(2L);
        dto.setAmount(60000.0);
        dto.setEffectiveDate(LocalDate.of(2024, 6, 1));
        dto.setSalaryType(Salary.SalaryType.INCREMENT);
        dto.setReason("Modified");

        assertEquals(2L, dto.getEmployeeId());
        assertEquals(60000.0, dto.getAmount());
        assertEquals(LocalDate.of(2024, 6, 1), dto.getEffectiveDate());
        assertEquals(Salary.SalaryType.INCREMENT, dto.getSalaryType());
        assertEquals("Modified", dto.getReason());
    }
}