package com.example.EmployeeManagementSystemAdvance.repository;

import com.example.EmployeeManagementSystemAdvance.entity.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void testSaveEmployeeWithNullSalary() {
        // The Employee entity allows null salary values
        // This is by design since salary is tracked in salaryHistory
        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setDepartment("IT");
        employee.setSalary(null);  // This is allowed
        
        // Should save successfully without throwing exception
        Employee savedEmployee = employeeRepository.save(employee);
        assertNotNull(savedEmployee.getId());
        assertNull(savedEmployee.getSalary());
        assertEquals("John", savedEmployee.getFirstName());
    }

    @Test
    void testSaveEmployeeWithValidSalary() {
        Employee employee = new Employee();
        employee.setFirstName("Jane");
        employee.setLastName("Smith");
        employee.setDepartment("HR");
        employee.setSalary(60000.0);
        
        Employee savedEmployee = employeeRepository.save(employee);
        assertNotNull(savedEmployee.getId());
        assertEquals(60000.0, savedEmployee.getSalary());
    }

    @Test
    void testSaveEmployeeWithZeroSalary() {
        Employee employee = new Employee();
        employee.setFirstName("Bob");
        employee.setLastName("Johnson");
        employee.setDepartment("Finance");
        employee.setSalary(0.0);
        
        Employee savedEmployee = employeeRepository.save(employee);
        assertNotNull(savedEmployee.getId());
        assertEquals(0.0, savedEmployee.getSalary());
    }

    @Test
    void testSaveEmployeeWithNegativeSalary() {
        // Note: If you want to prevent negative salaries, add a @Min(0) validation
        Employee employee = new Employee();
        employee.setFirstName("Alice");
        employee.setLastName("Williams");
        employee.setDepartment("Sales");
        employee.setSalary(-1000.0);
        
        Employee savedEmployee = employeeRepository.save(employee);
        assertNotNull(savedEmployee.getId());
        assertEquals(-1000.0, savedEmployee.getSalary());
    }

    @Test
    void testFindById() {
        Employee employee = new Employee();
        employee.setFirstName("Test");
        employee.setLastName("User");
        employee.setDepartment("Testing");
        employee.setSalary(45000.0);
        
        Employee savedEmployee = employeeRepository.save(employee);
        Long id = savedEmployee.getId();
        
        Employee foundEmployee = employeeRepository.findById(id).orElse(null);
        assertNotNull(foundEmployee);
        assertEquals("Test", foundEmployee.getFirstName());
        assertEquals(45000.0, foundEmployee.getSalary());
    }

    @Test
    void testDeleteEmployee() {
        Employee employee = new Employee();
        employee.setFirstName("Delete");
        employee.setLastName("Me");
        employee.setDepartment("Temp");
        
        Employee savedEmployee = employeeRepository.save(employee);
        Long id = savedEmployee.getId();
        
        employeeRepository.deleteById(id);
        
        assertFalse(employeeRepository.findById(id).isPresent());
    }

    @Test
    void testUpdateEmployee() {
        Employee employee = new Employee();
        employee.setFirstName("Original");
        employee.setLastName("Name");
        employee.setDepartment("IT");
        employee.setSalary(50000.0);
        
        Employee savedEmployee = employeeRepository.save(employee);
        Long id = savedEmployee.getId();
        
        savedEmployee.setFirstName("Updated");
        savedEmployee.setSalary(55000.0);
        employeeRepository.save(savedEmployee);
        
        Employee updatedEmployee = employeeRepository.findById(id).orElse(null);
        assertNotNull(updatedEmployee);
        assertEquals("Updated", updatedEmployee.getFirstName());
        assertEquals(55000.0, updatedEmployee.getSalary());
    }

    @Test
    void testSaveEmployeeWithAllNullFields() {
        Employee employee = new Employee();
        
        Employee savedEmployee = employeeRepository.save(employee);
        assertNotNull(savedEmployee.getId());
        assertNull(savedEmployee.getFirstName());
        assertNull(savedEmployee.getLastName());
        assertNull(savedEmployee.getDepartment());
        assertNull(savedEmployee.getSalary());
    }

    @Test
    void testSaveEmployeeWithEmptyStrings() {
        Employee employee = new Employee();
        employee.setFirstName("");
        employee.setLastName("");
        employee.setDepartment("");
        
        Employee savedEmployee = employeeRepository.save(employee);
        assertNotNull(savedEmployee.getId());
        assertEquals("", savedEmployee.getFirstName());
        assertEquals("", savedEmployee.getLastName());
        assertEquals("", savedEmployee.getDepartment());
    }

    @Test
    void testFindAll() {
        Employee emp1 = new Employee();
        emp1.setFirstName("First");
        emp1.setLastName("Employee");
        
        Employee emp2 = new Employee();
        emp2.setFirstName("Second");
        emp2.setLastName("Employee");
        
        employeeRepository.save(emp1);
        employeeRepository.save(emp2);
        
        long count = employeeRepository.count();
        assertTrue(count >= 2);
    }
}