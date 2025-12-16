package com.example.EmployeeManagementSystemAdvance.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    @Test
    void testNoArgsConstructor() {
        Employee employee = new Employee();
        assertNotNull(employee);
    }

    @Test
    void testAllArgsConstructor() {
        Employee employee = new Employee(1L, "John", "Doe", "IT", 75000.00);

        assertEquals(1L, employee.getId());
        assertEquals("John", employee.getFirstName());
        assertEquals("Doe", employee.getLastName());
        assertEquals("IT", employee.getDepartment());
        assertEquals(75000.00, employee.getSalary());
    }

    @Test
    void testSettersAndGetters() {
        Employee employee = new Employee();

        employee.setId(10L);
        employee.setFirstName("Alice");
        employee.setLastName("Smith");
        employee.setDepartment("HR");
        employee.setSalary(80000.00);

        assertEquals(10L, employee.getId());
        assertEquals("Alice", employee.getFirstName());
        assertEquals("Smith", employee.getLastName());
        assertEquals("HR", employee.getDepartment());
        assertEquals(80000.00, employee.getSalary());
    }

    @Test
    void testSetNullValues() {
        Employee employee = new Employee(1L, "Test", "User", "Dept", 50000.00);

        employee.setId(null);
        employee.setFirstName(null);
        employee.setLastName(null);
        employee.setDepartment(null);
        employee.setSalary(0.0);

        assertNull(employee.getId());
        assertNull(employee.getFirstName());
        assertNull(employee.getLastName());
        assertNull(employee.getDepartment());
        assertEquals(0.0, employee.getSalary());
    }

    @Test
    void testWithEmptyStrings() {
        Employee employee = new Employee(1L, "", "", "", 0.0);

        assertEquals(1L, employee.getId());
        assertEquals("", employee.getFirstName());
        assertEquals("", employee.getLastName());
        assertEquals("", employee.getDepartment());
        assertEquals(0.0, employee.getSalary());
    }

    @Test
    void testNegativeSalary() {
        Employee employee = new Employee();
        employee.setSalary(-1000.00);
        assertEquals(-1000.00, employee.getSalary());
    }

    @Test
    void testLargeSalary() {
        Employee employee = new Employee();
        employee.setSalary(999_999_999.99);
        assertEquals(999_999_999.99, employee.getSalary());
    }

    @Test
    void testEqualsAndHashCode() {
        Employee emp1 = new Employee(1L, "John", "Doe", "IT", 75000.00);
        Employee emp2 = new Employee(1L, "John", "Doe", "IT", 75000.00);
        Employee emp3 = new Employee(2L, "Jane", "Doe", "HR", 80000.00);

        assertEquals(emp1, emp2);
        assertNotEquals(emp1, emp3);
        assertEquals(emp1.hashCode(), emp2.hashCode());
    }

    @Test
    void testToString() {
        Employee employee = new Employee(1L, "John", "Doe", "IT", 75000.00);
        String str = employee.toString();

        assertNotNull(str);
        assertTrue(str.contains("John"));
        assertTrue(str.contains("Doe"));
        assertTrue(str.contains("IT"));
        assertTrue(str.contains("75000"));
    }

    @Test
    void testEqualsWithSameReference() {
        Employee employee = new Employee(1L, "John", "Doe", "IT", 75000.00);
        assertEquals(employee, employee);
    }

    @Test
    void testEqualsWithNull() {
        Employee employee = new Employee(1L, "John", "Doe", "IT", 75000.00);
        assertNotEquals(employee, null);
    }

    @Test
    void testEqualsWithDifferentClass() {
        Employee employee = new Employee(1L, "John", "Doe", "IT", 75000.00);
        assertNotEquals(employee, "Some String");
    }

    @Test
    void testMultipleInstances() {
        Employee emp1 = new Employee(1L, "Employee", "One", "IT", 50000.00);
        Employee emp2 = new Employee(2L, "Employee", "Two", "HR", 60000.00);

        assertNotEquals(emp1, emp2);
    }

    @Test
    void testSalaryWithDecimal() {
        Employee employee = new Employee();
        employee.setSalary(55000.75);
        assertEquals(55000.75, employee.getSalary());
    }

    @Test
    void testWithSpecialCharacters() {
        Employee employee = new Employee(1L, "John", "O'Connor", "R&D", 75000.00);

        assertEquals("John", employee.getFirstName());
        assertEquals("O'Connor", employee.getLastName());
        assertEquals("R&D", employee.getDepartment());
    }

    @Test
    void testCanEqualIndirectly() {
        Employee emp1 = new Employee(1L, "John", "Doe", "IT", 75000.00);
        Employee emp2 = new Employee(1L, "John", "Doe", "IT", 75000.00);
        assertTrue(emp1.equals(emp2));
    }

    @Test
    void testHashCodeConsistency() {
        Employee emp = new Employee(1L, "A", "B", "C", 1000.0);
        int hash1 = emp.hashCode();
        int hash2 = emp.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void testIdSetterCoverage() {
        Employee emp = new Employee();
        emp.setId(100L);
        assertEquals(100L, emp.getId());
    }

    @Test
    void testSettersWithMixedValues() {
        Employee emp = new Employee();
        emp.setFirstName("Test");
        emp.setLastName(null);
        emp.setDepartment("IT");
        emp.setSalary(0.0);

        assertEquals("Test", emp.getFirstName());
        assertNull(emp.getLastName());
        assertEquals("IT", emp.getDepartment());
        assertEquals(0.0, emp.getSalary());
    }

    // NEW: Additional tests for complete coverage
    @Test
    void testEqualsWithDifferentId() {
        Employee emp1 = new Employee(1L, "John", "Doe", "IT", 75000.00);
        Employee emp2 = new Employee(2L, "John", "Doe", "IT", 75000.00);
        assertNotEquals(emp1, emp2);
    }

    @Test
    void testEqualsWithDifferentFirstName() {
        Employee emp1 = new Employee(1L, "John", "Doe", "IT", 75000.00);
        Employee emp2 = new Employee(1L, "Jane", "Doe", "IT", 75000.00);
        assertNotEquals(emp1, emp2);
    }

    @Test
    void testEqualsWithDifferentLastName() {
        Employee emp1 = new Employee(1L, "John", "Doe", "IT", 75000.00);
        Employee emp2 = new Employee(1L, "John", "Smith", "IT", 75000.00);
        assertNotEquals(emp1, emp2);
    }

    @Test
    void testEqualsWithDifferentDepartment() {
        Employee emp1 = new Employee(1L, "John", "Doe", "IT", 75000.00);
        Employee emp2 = new Employee(1L, "John", "Doe", "HR", 75000.00);
        assertNotEquals(emp1, emp2);
    }

    @Test
    void testEqualsWithDifferentSalary() {
        Employee emp1 = new Employee(1L, "John", "Doe", "IT", 75000.00);
        Employee emp2 = new Employee(1L, "John", "Doe", "IT", 80000.00);
        assertNotEquals(emp1, emp2);
    }

    @Test
    void testEqualsWithNullFields() {
        Employee emp1 = new Employee(null, null, null, null, 0.0);
        Employee emp2 = new Employee(null, null, null, null, 0.0);
        assertEquals(emp1, emp2);
    }

    @Test
    void testHashCodeWithNullFields() {
        Employee emp1 = new Employee(null, null, null, null, 0.0);
        Employee emp2 = new Employee(null, null, null, null, 0.0);
        assertEquals(emp1.hashCode(), emp2.hashCode());
    }

    @Test
    void testToStringWithNullFields() {
        Employee employee = new Employee(null, null, null, null, 0.0);
        String str = employee.toString();
        assertNotNull(str);
        assertTrue(str.contains("null") || str.contains("Employee"));
    }

    @Test
    void testZeroSalary() {
        Employee employee = new Employee();
        employee.setSalary(0.0);
        assertEquals(0.0, employee.getSalary());
    }

    @Test
    void testVeryLongNames() {
        String longName = "A".repeat(100);
        Employee employee = new Employee(1L, longName, longName, "IT", 75000.00);
        assertEquals(longName, employee.getFirstName());
        assertEquals(longName, employee.getLastName());
    }
}