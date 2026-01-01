package com.example.EmployeeManagementSystemAdvance.repository;

import com.example.EmployeeManagementSystemAdvance.entity.Employee;
import com.example.EmployeeManagementSystemAdvance.entity.Salary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class SalaryRepositoryTest {

    @Autowired
    private SalaryRepository salaryRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Employee employee;
    private Salary oldSalary;
    private Salary currentSalary;

    @BeforeEach
    void setUp() {
        // Clean database before each test
        salaryRepository.deleteAll();
        employeeRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
        
        // Create test employee
        employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setDepartment("IT");
        employee.setSalary(55000.0);
        employee = employeeRepository.save(employee);

        // Create old salary record
        oldSalary = new Salary();
        oldSalary.setEmployee(employee);
        oldSalary.setAmount(50000.0);
        oldSalary.setEffectiveDate(LocalDate.of(2024, 1, 1));
        oldSalary.setEndDate(LocalDate.of(2024, 6, 30));
        oldSalary.setSalaryType(Salary.SalaryType.BASE_SALARY);
        oldSalary.setReason("Initial");
        oldSalary.setCurrent(false);
        oldSalary = salaryRepository.save(oldSalary);

        // Create current salary record
        currentSalary = new Salary();
        currentSalary.setEmployee(employee);
        currentSalary.setAmount(55000.0);
        currentSalary.setEffectiveDate(LocalDate.of(2024, 7, 1));
        currentSalary.setEndDate(null);
        currentSalary.setSalaryType(Salary.SalaryType.INCREMENT);
        currentSalary.setReason("Increment");
        currentSalary.setCurrent(true);
        currentSalary = salaryRepository.save(currentSalary);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testFindByEmployeeId() {
        List<Salary> list = salaryRepository.findByEmployeeId(employee.getId());
        assertEquals(2, list.size());
        assertTrue(list.get(0).getEffectiveDate()
                .isAfter(list.get(1).getEffectiveDate()));
        // Verify employee is eagerly loaded
        assertNotNull(list.get(0).getEmployee());
        assertNotNull(list.get(0).getEmployee().getFirstName());
    }

    @Test
    void testFindCurrentSalaryByEmployeeIdWithEmployee() {
        Optional<Salary> current = salaryRepository
                .findCurrentSalaryByEmployeeIdWithEmployee(employee.getId());

        assertTrue(current.isPresent());
        assertTrue(current.get().isCurrent());
        assertEquals(55000.0, current.get().getAmount());
        assertNull(current.get().getEndDate());
        // Verify employee is eagerly loaded
        assertNotNull(current.get().getEmployee());
        assertEquals("John", current.get().getEmployee().getFirstName());
    }

    @Test
    void testFindCurrentSalariesByDepartmentWithEmployee() {
        List<Salary> list =
                salaryRepository.findCurrentSalariesByDepartmentWithEmployee("IT");

        assertEquals(1, list.size());
        assertTrue(list.get(0).isCurrent());
        // Verify employee is eagerly loaded
        assertNotNull(list.get(0).getEmployee());
        assertEquals("IT", list.get(0).getEmployee().getDepartment());
    }

    @Test
    void testFindAllCurrentSalariesWithEmployee() {
        List<Salary> list = salaryRepository.findAllCurrentSalariesWithEmployee();
        assertEquals(1, list.size());
        // Verify employee is eagerly loaded
        assertNotNull(list.get(0).getEmployee());
        assertNotNull(list.get(0).getEmployee().getFirstName());
    }

    @Test
    void testFindByIdWithEmployee() {
        Optional<Salary> result = salaryRepository.findByIdWithEmployee(currentSalary.getId());
        
        assertTrue(result.isPresent());
        assertEquals(currentSalary.getId(), result.get().getId());
        // Verify employee is eagerly loaded
        assertNotNull(result.get().getEmployee());
        assertEquals("John", result.get().getEmployee().getFirstName());
    }

    @Test
    void testFindAllWithEmployee() {
        List<Salary> list = salaryRepository.findAllWithEmployee();
        
        assertEquals(2, list.size());
        // Verify all employees are eagerly loaded
        list.forEach(salary -> {
            assertNotNull(salary.getEmployee());
            assertNotNull(salary.getEmployee().getFirstName());
        });
    }

    @Test
    void testGetDepartmentSalaryStatistics() {
        List<Object[]> stats =
                salaryRepository.getDepartmentSalaryStatistics();

        assertFalse(stats.isEmpty());

        Object[] row = stats.get(0);
        assertNotNull(row[0]); // department
        assertTrue(row[1] instanceof Long);
        assertTrue(row[2] instanceof Double);
    }

    @Test
    void testFindSalariesInDateRangeWithEmployee() {
        List<Salary> list = salaryRepository.findSalariesInDateRangeWithEmployee(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31)
        );

        assertEquals(2, list.size());
        // Verify employees are eagerly loaded
        list.forEach(salary -> {
            assertNotNull(salary.getEmployee());
            assertNotNull(salary.getEmployee().getFirstName());
        });
    }

    @Test
    void testFindBySalaryType() {
        List<Salary> increments =
                salaryRepository.findBySalaryType(Salary.SalaryType.INCREMENT);
        assertEquals(1, increments.size());
        assertEquals(55000.0, increments.get(0).getAmount());

        List<Salary> base =
                salaryRepository.findBySalaryType(Salary.SalaryType.BASE_SALARY);
        assertEquals(0, base.size()); // No current BASE_SALARY records exist
        
        // Test with a current BASE_SALARY record
        Salary currentBase = new Salary();
        currentBase.setEmployee(employee);
        currentBase.setAmount(52000.0);
        currentBase.setEffectiveDate(LocalDate.of(2024, 8, 1));
        currentBase.setSalaryType(Salary.SalaryType.BASE_SALARY);
        currentBase.setCurrent(true);
        salaryRepository.save(currentBase);
        
        entityManager.flush();
        entityManager.clear();
        
        List<Salary> baseAfter = salaryRepository.findBySalaryType(Salary.SalaryType.BASE_SALARY);
        assertEquals(1, baseAfter.size());
    }

    @Test
    void testHasActiveSalary() {
        assertTrue(salaryRepository.hasActiveSalary(employee.getId()));
    }

    @Test
    void testGetTotalSalaryExpenditure() {
        Double total = salaryRepository.getTotalSalaryExpenditure();
        assertEquals(55000.0, total);
    }

    @Test
    void testMultipleEmployeesWithCurrentSalaries() {
        Employee employee2 = new Employee();
        employee2.setFirstName("Jane");
        employee2.setLastName("Smith");
        employee2.setDepartment("HR");
        employee2.setSalary(60000.0);
        employee2 = employeeRepository.save(employee2);

        Salary salary2 = new Salary();
        salary2.setEmployee(employee2);
        salary2.setAmount(60000.0);
        salary2.setEffectiveDate(LocalDate.of(2024, 7, 1));
        salary2.setSalaryType(Salary.SalaryType.BASE_SALARY);
        salary2.setCurrent(true);

        salaryRepository.save(salary2);

        entityManager.flush();
        entityManager.clear();

        Double total = salaryRepository.getTotalSalaryExpenditure();
        assertEquals(115000.0, total);
    }

    @Test
    void testSalaryTypeEnumValues() {
        assertTrue(Salary.SalaryType.values().length >= 1);
        assertNotNull(Salary.SalaryType.BASE_SALARY);
    }

    @Test
    void testFindByEmployeeAndCurrentWithEmployee() {
        List<Salary> currentSalaries = salaryRepository
                .findByEmployeeAndCurrentWithEmployee(employee, true);
        
        assertEquals(1, currentSalaries.size());
        assertTrue(currentSalaries.get(0).isCurrent());
        // Verify employee is eagerly loaded
        assertNotNull(currentSalaries.get(0).getEmployee());
        assertEquals(employee.getId(), currentSalaries.get(0).getEmployee().getId());
        
        List<Salary> historicalSalaries = salaryRepository
                .findByEmployeeAndCurrentWithEmployee(employee, false);
        
        assertEquals(1, historicalSalaries.size());
        assertFalse(historicalSalaries.get(0).isCurrent());
    }

    @Test
    void testFindByEmployeeOrderByEffectiveDateDescWithEmployee() {
        List<Salary> salaries = salaryRepository
                .findByEmployeeOrderByEffectiveDateDescWithEmployee(employee);
        
        assertEquals(2, salaries.size());
        // Verify descending order
        assertTrue(salaries.get(0).getEffectiveDate()
                .isAfter(salaries.get(1).getEffectiveDate()));
        // Verify employees are eagerly loaded
        salaries.forEach(salary -> {
            assertNotNull(salary.getEmployee());
            assertEquals(employee.getId(), salary.getEmployee().getId());
        });
    }
}