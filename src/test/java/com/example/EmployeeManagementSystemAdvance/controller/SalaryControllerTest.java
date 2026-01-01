package com.example.EmployeeManagementSystemAdvance.controller;

import com.example.EmployeeManagementSystemAdvance.dto.*;
import com.example.EmployeeManagementSystemAdvance.entity.Salary;
import com.example.EmployeeManagementSystemAdvance.service.SalaryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SalaryController.class)
class SalaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SalaryService salaryService;

    private SalaryRequestDTO requestDTO;
    private SalaryResponseDTO responseDTO;
    private SalaryHistoryDTO historyDTO;
    private DepartmentSalaryStatsDTO statsDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new SalaryRequestDTO();
        requestDTO.setEmployeeId(1L);
        requestDTO.setAmount(50000.0);
        requestDTO.setEffectiveDate(LocalDate.of(2024, 1, 1));
        requestDTO.setSalaryType(Salary.SalaryType.BASE_SALARY);
        requestDTO.setReason("Initial salary");

        responseDTO = new SalaryResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setEmployeeId(1L);
        responseDTO.setEmployeeName("John Doe");
        responseDTO.setDepartment("IT");
        responseDTO.setAmount(50000.0);
        responseDTO.setCurrent(true);
        responseDTO.setMessage("Success");

        historyDTO = new SalaryHistoryDTO();
        historyDTO.setEmployeeId(1L);
        historyDTO.setEmployeeName("John Doe");
        historyDTO.setCurrentSalary(50000.0);

        statsDTO = new DepartmentSalaryStatsDTO();
        statsDTO.setDepartment("IT");
        statsDTO.setEmployeeCount(5L);
        statsDTO.setAverageSalary(55000.0);
        statsDTO.setMinSalary(45000.0);
        statsDTO.setMaxSalary(70000.0);
        statsDTO.setTotalSalary(275000.0);
    }

    @Test
    void createSalary_Success() throws Exception {
        when(salaryService.createSalary(any(SalaryRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/salaries/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(50000.0));

        verify(salaryService, times(1)).createSalary(any(SalaryRequestDTO.class));
    }

    @Test
    void createSalary_WithoutAmount_Success() throws Exception {
        // Create salary request with null amount
        SalaryRequestDTO dtoWithoutAmount = new SalaryRequestDTO();
        dtoWithoutAmount.setEmployeeId(1L);
        dtoWithoutAmount.setAmount(null);
        dtoWithoutAmount.setEffectiveDate(LocalDate.of(2024, 1, 1));
        dtoWithoutAmount.setSalaryType(Salary.SalaryType.BASE_SALARY);
        dtoWithoutAmount.setReason("Initial salary");

        SalaryResponseDTO response = new SalaryResponseDTO();
        response.setId(1L);
        response.setAmount(61200.0); // Amount fetched from service

        when(salaryService.createSalary(any(SalaryRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/salaries/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoWithoutAmount)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(61200.0));

        verify(salaryService, times(1)).createSalary(any(SalaryRequestDTO.class));
    }

    @Test
    void getSalaryById_Success() throws Exception {
        when(salaryService.getSalaryById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/salaries/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(50000.0));

        verify(salaryService, times(1)).getSalaryById(1L);
    }

    @Test
    void getAllSalaries_Success() throws Exception {
        List<SalaryResponseDTO> salaries = Arrays.asList(responseDTO);
        when(salaryService.getAllSalaries()).thenReturn(salaries);

        mockMvc.perform(get("/api/v1/salaries/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(salaryService, times(1)).getAllSalaries();
    }

    @Test
    void getAllCurrentSalaries_Success() throws Exception {
        List<SalaryResponseDTO> salaries = Arrays.asList(responseDTO);
        when(salaryService.getAllCurrentSalaries()).thenReturn(salaries);

        mockMvc.perform(get("/api/v1/salaries/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].current").value(true));

        verify(salaryService, times(1)).getAllCurrentSalaries();
    }

    @Test
    void getEmployeeSalaryHistory_Success() throws Exception {
        when(salaryService.getEmployeeSalaryHistory(1L)).thenReturn(historyDTO);

        mockMvc.perform(get("/api/v1/salaries/employee/1/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value(1))
                .andExpect(jsonPath("$.currentSalary").value(50000.0));

        verify(salaryService, times(1)).getEmployeeSalaryHistory(1L);
    }

    @Test
    void getCurrentSalary_Success() throws Exception {
        when(salaryService.getCurrentSalaryByEmployeeId(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/salaries/employee/1/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current").value(true));

        verify(salaryService, times(1)).getCurrentSalaryByEmployeeId(1L);
    }

    @Test
    void processSalaryIncrement_Success() throws Exception {
        SalaryIncrementRequestDTO incrementDTO = new SalaryIncrementRequestDTO();
        incrementDTO.setEmployeeId(1L);
        incrementDTO.setIncrementPercentage(10.0);
        incrementDTO.setEffectiveDate(LocalDate.of(2024, 7, 1));

        when(salaryService.processSalaryIncrement(any(SalaryIncrementRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/salaries/increment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incrementDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(salaryService, times(1)).processSalaryIncrement(any(SalaryIncrementRequestDTO.class));
    }

    @Test
    void updateSalary_Success() throws Exception {
        when(salaryService.updateSalary(eq(1L), any(SalaryRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/salaries/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(salaryService, times(1)).updateSalary(eq(1L), any(SalaryRequestDTO.class));
    }

    @Test
    void getSalariesByDepartment_Success() throws Exception {
        List<SalaryResponseDTO> salaries = Arrays.asList(responseDTO);
        when(salaryService.getSalariesByDepartment("IT")).thenReturn(salaries);

        mockMvc.perform(get("/api/v1/salaries/department/IT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].department").value("IT"));

        verify(salaryService, times(1)).getSalariesByDepartment("IT");
    }

    @Test
    void getDepartmentStatistics_Success() throws Exception {
        List<DepartmentSalaryStatsDTO> stats = Arrays.asList(statsDTO);
        when(salaryService.getDepartmentSalaryStatistics()).thenReturn(stats);

        mockMvc.perform(get("/api/v1/salaries/statistics/department"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].department").value("IT"))
                .andExpect(jsonPath("$[0].employeeCount").value(5));

        verify(salaryService, times(1)).getDepartmentSalaryStatistics();
    }

    @Test
    void getSalariesInDateRange_Success() throws Exception {
        List<SalaryResponseDTO> salaries = Arrays.asList(responseDTO);
        when(salaryService.getSalariesInDateRange(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(salaries);

        mockMvc.perform(get("/api/v1/salaries/date-range")
                        .param("startDate", "2024-01-01")
                        .param("endDate", "2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(salaryService, times(1)).getSalariesInDateRange(any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void getTotalSalaryExpenditure_Success() throws Exception {
        when(salaryService.getTotalSalaryExpenditure()).thenReturn(275000.0);

        mockMvc.perform(get("/api/v1/salaries/total-expenditure"))
                .andExpect(status().isOk())
                .andExpect(content().string("275000.0"));

        verify(salaryService, times(1)).getTotalSalaryExpenditure();
    }

    @Test
    void deleteSalary_Success() throws Exception {
        doNothing().when(salaryService).deleteSalary(1L);

        mockMvc.perform(delete("/api/v1/salaries/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Salary record deleted successfully"));

        verify(salaryService, times(1)).deleteSalary(1L);
    }
    @Test
    void createSalary_NullAmount_FetchesFromEmployee_Success() throws Exception {
        // SalaryRequestDTO with null amount
        SalaryRequestDTO dtoWithNullAmount = new SalaryRequestDTO();
        dtoWithNullAmount.setEmployeeId(1L);
        dtoWithNullAmount.setAmount(null);
        dtoWithNullAmount.setEffectiveDate(LocalDate.of(2024, 1, 1));
        dtoWithNullAmount.setSalaryType(Salary.SalaryType.BASE_SALARY);
        dtoWithNullAmount.setReason("Initial salary");

        // Mocked response: service layer sets the amount from employee
        SalaryResponseDTO responseFromService = new SalaryResponseDTO();
        responseFromService.setId(1L);
        responseFromService.setEmployeeId(1L);
        responseFromService.setEmployeeName("John Doe");
        responseFromService.setDepartment("IT");
        responseFromService.setAmount(61200.0); // Amount fetched from employee
        responseFromService.setCurrent(true);
        responseFromService.setMessage("Success");

        // When the service is called with any SalaryRequestDTO, return the mocked response
        when(salaryService.createSalary(any(SalaryRequestDTO.class)))
                .thenReturn(responseFromService);

        mockMvc.perform(post("/api/v1/salaries/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoWithNullAmount)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.employeeId").value(1))
                .andExpect(jsonPath("$.amount").value(61200.0))
                .andExpect(jsonPath("$.message").value("Success"));

        // Verify that service layer createSalary() was called exactly once
        verify(salaryService, times(1)).createSalary(any(SalaryRequestDTO.class));
    }

}
