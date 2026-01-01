package com.example.EmployeeManagementSystemAdvance.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SalaryTest {

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
    }

    @ParameterizedTest
    @EnumSource(Salary.SalaryType.class)
    void testSalaryTypes(Salary.SalaryType type) {
        Salary salary = new Salary();
        salary.setSalaryType(type);
        assertEquals(type, salary.getSalaryType());
    }

    @Test
    void testNoArgConstructorDefaults() {
        Salary salary = new Salary();

        assertNull(salary.getId());
        assertNull(salary.getEmployee());
        assertNull(salary.getAmount());
        assertNull(salary.getEffectiveDate());
        assertNull(salary.getEndDate());
        assertNull(salary.getSalaryType());
        assertNull(salary.getReason());
        assertTrue(salary.isCurrent());
        assertNull(salary.getCreatedAt());
        assertNull(salary.getUpdatedAt());
    }

    @Test
    void testSettersAndGetters() {
        LocalDate eff = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 12, 31);
        LocalDateTime now = LocalDateTime.now();

        Salary salary = new Salary();
        salary.setId(1L);
        salary.setEmployee(employee);
        salary.setAmount(50000.0);
        salary.setEffectiveDate(eff);
        salary.setEndDate(end);
        salary.setSalaryType(Salary.SalaryType.BASE_SALARY);
        salary.setReason("Initial");
        salary.setCurrent(true);
        salary.setCreatedAt(now);
        salary.setUpdatedAt(now);

        assertEquals(1L, salary.getId());
        assertEquals(employee, salary.getEmployee());
        assertEquals(50000.0, salary.getAmount());
        assertEquals(eff, salary.getEffectiveDate());
        assertEquals(end, salary.getEndDate());
        assertEquals(Salary.SalaryType.BASE_SALARY, salary.getSalaryType());
        assertEquals("Initial", salary.getReason());
        assertTrue(salary.isCurrent());
        assertEquals(now, salary.getCreatedAt());
        assertEquals(now, salary.getUpdatedAt());
    }

    @Test
    void testCurrentFlagDefaultsToTrue() {
        Salary salary = new Salary();
        assertTrue(salary.isCurrent());
    }

    @Test
    void testCurrentFlagCanBeChanged() {
        Salary salary = new Salary();
        salary.setCurrent(false);
        assertFalse(salary.isCurrent());

        salary.setCurrent(true);
        assertTrue(salary.isCurrent());
    }

    @Test
    void testEndDateForHistoricalSalary() {
        Salary salary = new Salary();
        salary.setEffectiveDate(LocalDate.of(2024, 1, 1));
        salary.setEndDate(LocalDate.of(2024, 12, 31));
        salary.setCurrent(false);

        assertFalse(salary.isCurrent());
        assertTrue(salary.getEndDate().isAfter(salary.getEffectiveDate()));
    }

    @Test
    void testEndDateBeforeEffectiveDateAllowed() {
        Salary salary = new Salary();
        salary.setEffectiveDate(LocalDate.of(2024, 5, 1));
        salary.setEndDate(LocalDate.of(2024, 4, 30));

        assertTrue(salary.getEndDate().isBefore(salary.getEffectiveDate()));
    }

    @Test
    void testToStringContainsFields() {
        Salary salary = new Salary();
        salary.setId(1L);
        salary.setAmount(50000.0);
        salary.setSalaryType(Salary.SalaryType.BASE_SALARY);

        String str = salary.toString();
        assertTrue(str.contains("id=1"));
        assertTrue(str.contains("amount=50000"));
    }

    @Test
    void testEqualsAndHashCode() {
        Salary s1 = new Salary();
        s1.setId(1L);
        s1.setAmount(50000.0);

        Salary s2 = new Salary();
        s2.setId(1L);
        s2.setAmount(50000.0);

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testSalaryIncrementScenario() {
        Salary oldSalary = new Salary();
        oldSalary.setEmployee(employee);
        oldSalary.setAmount(50000.0);
        oldSalary.setEffectiveDate(LocalDate.of(2024, 1, 1));
        oldSalary.setSalaryType(Salary.SalaryType.BASE_SALARY);
        oldSalary.setCurrent(false);
        oldSalary.setEndDate(LocalDate.of(2024, 12, 31));

        Salary newSalary = new Salary();
        newSalary.setEmployee(employee);
        newSalary.setAmount(60000.0);
        newSalary.setEffectiveDate(LocalDate.of(2025, 1, 1));
        newSalary.setSalaryType(Salary.SalaryType.INCREMENT);
        newSalary.setCurrent(true);

        assertFalse(oldSalary.isCurrent());
        assertEquals(60000.0, newSalary.getAmount());
        assertTrue(newSalary.isCurrent());
        assertNull(newSalary.getEndDate());
    }

    @Test
    void testAllSalaryTypes() {
        assertEquals(5, Salary.SalaryType.values().length);

        assertNotNull(Salary.SalaryType.BASE_SALARY);
        assertNotNull(Salary.SalaryType.INCREMENT);
        assertNotNull(Salary.SalaryType.ADJUSTMENT);
        assertNotNull(Salary.SalaryType.BONUS);
        assertNotNull(Salary.SalaryType.DEDUCTION);
    }

    @Test
    void testPrePersistSetsCreatedAtAndUpdatedAt() {
        Salary salary = new Salary();
        assertNull(salary.getCreatedAt());
        assertNull(salary.getUpdatedAt());

        // Simulate @PrePersist
        salary.onCreate();

        assertNotNull(salary.getCreatedAt());
        assertNotNull(salary.getUpdatedAt());
        assertEquals(salary.getCreatedAt(), salary.getUpdatedAt());
    }

    @Test
    void testPreUpdateSetsUpdatedAt() throws InterruptedException {
        Salary salary = new Salary();
        salary.onCreate();

        LocalDateTime originalCreatedAt = salary.getCreatedAt();
        LocalDateTime originalUpdatedAt = salary.getUpdatedAt();

        // Wait a bit to ensure time difference
        Thread.sleep(10);

        // Simulate @PreUpdate
        salary.onUpdate();

        assertEquals(originalCreatedAt, salary.getCreatedAt());
        assertNotEquals(originalUpdatedAt, salary.getUpdatedAt());
        assertTrue(salary.getUpdatedAt().isAfter(originalUpdatedAt));
    }

    @Test
    void testCreatedAtIsImmutableAfterPersist() {
        Salary salary = new Salary();
        salary.onCreate();

        LocalDateTime originalCreatedAt = salary.getCreatedAt();

        salary.onUpdate();
        salary.onUpdate();
        salary.onUpdate();

        assertEquals(originalCreatedAt, salary.getCreatedAt());
    }

    @Test
    void testTimestampFields() {
        LocalDateTime now = LocalDateTime.now();
        
        Salary salary = new Salary();
        salary.setCreatedAt(now);
        salary.setUpdatedAt(now);

        assertEquals(now, salary.getCreatedAt());
        assertEquals(now, salary.getUpdatedAt());
    }

    @Test
    void testAllArgsConstructorWithTimestamps() {
        LocalDate effectiveDate = LocalDate.of(2024, 1, 1);
        LocalDateTime now = LocalDateTime.now();

        Salary salary = new Salary(
            1L,
            employee,
            50000.0,
            effectiveDate,
            null,
            Salary.SalaryType.BASE_SALARY,
            "Initial salary",
            true,
            now,
            now
        );

        assertEquals(1L, salary.getId());
        assertEquals(employee, salary.getEmployee());
        assertEquals(50000.0, salary.getAmount());
        assertEquals(effectiveDate, salary.getEffectiveDate());
        assertNull(salary.getEndDate());
        assertEquals(Salary.SalaryType.BASE_SALARY, salary.getSalaryType());
        assertEquals("Initial salary", salary.getReason());
        assertTrue(salary.isCurrent());
        assertEquals(now, salary.getCreatedAt());
        assertEquals(now, salary.getUpdatedAt());
    }

    @Test
    void testNullTimestamps() {
        Salary salary = new Salary();
        
        assertNull(salary.getCreatedAt());
        assertNull(salary.getUpdatedAt());

        salary.setCreatedAt(null);
        salary.setUpdatedAt(null);

        assertNull(salary.getCreatedAt());
        assertNull(salary.getUpdatedAt());
    }
}