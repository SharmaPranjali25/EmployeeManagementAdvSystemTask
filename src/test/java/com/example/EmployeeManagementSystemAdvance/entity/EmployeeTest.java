package com.example.EmployeeManagementSystemAdvance.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    private Employee employee;
    private Salary salary;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setDepartment("IT");
        employee.setSalary(50000.0);

        salary = new Salary();
        salary.setId(1L);
        salary.setAmount(50000.0);
    }

    @Test
    void testAddSalary() {
        employee.addSalary(salary);
        assertTrue(employee.getSalaryHistory().contains(salary));
        assertEquals(employee, salary.getEmployee());
    }

    @Test
    void testAddSalary_NullSalary_Throws() {
        assertThrows(NullPointerException.class, () -> employee.addSalary(null));
    }

    @Test
    void testAddSalary_WhenHistoryNull() {
        Employee emp = new Employee();
        emp.setSalaryHistory(null);
        Salary s = new Salary();
        emp.addSalary(s);
        assertNotNull(emp.getSalaryHistory());
        assertTrue(emp.getSalaryHistory().contains(s));
    }

    @Test
    void testRemoveSalary() {
        employee.addSalary(salary);
        employee.removeSalary(salary);
        assertFalse(employee.getSalaryHistory().contains(salary));
        assertNull(salary.getEmployee());
    }

    @Test
    void testRemoveSalary_NotExists() {
        employee.removeSalary(salary);
        assertFalse(employee.getSalaryHistory().contains(salary));
    }

    @Test
    void testRemoveSalary_FromMultiple() {
        Salary s2 = new Salary();
        s2.setId(2L);
        employee.addSalary(salary);
        employee.addSalary(s2);
        employee.removeSalary(salary);

        assertFalse(employee.getSalaryHistory().contains(salary));
        assertTrue(employee.getSalaryHistory().contains(s2));
        assertEquals(1, employee.getSalaryHistory().size());
    }

    @Test
    void testGettersSettersAndConstructors() {
        Employee emp1 = new Employee(1L, "John", "Doe", "IT", 50000.0, new ArrayList<>());
        assertEquals(1L, emp1.getId());
        assertEquals("John", emp1.getFirstName());
        assertEquals("Doe", emp1.getLastName());
        assertEquals("IT", emp1.getDepartment());
        assertEquals(50000.0, emp1.getSalary());
        assertNotNull(emp1.getSalaryHistory());

        Employee emp2 = new Employee();
        assertNull(emp2.getId());
        assertNull(emp2.getFirstName());
        assertNull(emp2.getLastName());
        assertNull(emp2.getDepartment());
        assertNull(emp2.getSalary());
    }

    @Test
    void testSalaryEdgeCases() {
        employee.setSalary(0.0);
        assertEquals(0.0, employee.getSalary());
        employee.setSalary(-1000.0);
        assertEquals(-1000.0, employee.getSalary());
        
        employee.setSalary(null);
        assertNull(employee.getSalary());
    }

    @Test
    void testDepartmentChange() {
        employee.setDepartment("HR");
        assertEquals("HR", employee.getDepartment());
        employee.setDepartment("Finance");
        assertEquals("Finance", employee.getDepartment());
    }

    @Test
    void testEqualsAndHashCode() {
        Employee emp1 = new Employee(1L, "John", "Doe", "IT", 50000.0, null);
        Employee emp2 = new Employee(1L, "John", "Doe", "IT", 50000.0, null);

        assertEquals(emp1, emp2);
        assertEquals(emp1.hashCode(), emp2.hashCode());
        assertEquals(employee, employee);
        assertEquals(employee.hashCode(), employee.hashCode());
    }

    @Test
    void testToStringContainsAll() {
        String str = employee.toString();
        assertTrue(str.contains("id=1"));
        assertTrue(str.contains("firstName=John"));
        assertTrue(str.contains("lastName=Doe"));
    }

    @Test
    void testFullName() {
        assertEquals("John Doe", employee.getFirstName() + " " + employee.getLastName());
    }

    @Test
    void testSalaryHistoryInitialization() {
        Employee emp = new Employee();
        assertNotNull(emp.getSalaryHistory());
        assertTrue(emp.getSalaryHistory().isEmpty());
    }

    @Test
    void testNullAndEmptyFields() {
        Employee emp = new Employee();
        emp.setFirstName(null);
        emp.setLastName(null);
        emp.setDepartment(null);
        assertNull(emp.getFirstName());
        assertNull(emp.getLastName());
        assertNull(emp.getDepartment());

        employee.setFirstName("");
        employee.setLastName("");
        employee.setDepartment("");
        assertEquals("", employee.getFirstName());
        assertEquals("", employee.getLastName());
        assertEquals("", employee.getDepartment());
    }
}