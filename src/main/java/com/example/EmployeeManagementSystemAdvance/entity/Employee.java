package com.example.EmployeeManagementSystemAdvance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String department;

    // Nullable - allows null salary (current/latest salary is optional)
    // The actual salary history is tracked in the salaryHistory relationship
    private Double salary;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Salary> salaryHistory = new ArrayList<>();

    // Helper method to add salary
    public void addSalary(Salary salary) {
        if (salary == null) {
            throw new NullPointerException("Salary cannot be null");
        }
        if (salaryHistory == null) {
            salaryHistory = new ArrayList<>();
        }
        salaryHistory.add(salary);
        salary.setEmployee(this);
    }

    // Helper method to remove salary
    public void removeSalary(Salary salary) {
        if (salaryHistory != null) {
            salaryHistory.remove(salary);
            if (salary != null) {
                salary.setEmployee(null);
            }
        }
    }
}