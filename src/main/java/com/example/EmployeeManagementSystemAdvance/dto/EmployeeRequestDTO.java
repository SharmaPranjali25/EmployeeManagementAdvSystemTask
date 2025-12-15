package com.example.EmployeeManagementSystemAdvance.dto;

import lombok.Data;

@Data
public class EmployeeRequestDTO {

    private String name;
    private String department;
    private double salary; // included here because client can send it
}
