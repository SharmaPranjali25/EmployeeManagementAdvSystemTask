package com.example.EmployeeManagementSystemAdvance.mapper;

import com.example.EmployeeManagementSystemAdvance.dto.EmployeeRequestDTO;
import com.example.EmployeeManagementSystemAdvance.dto.EmployeeResponseDTO;
import com.example.EmployeeManagementSystemAdvance.dto.EmployeeResponseV2DTO;
import com.example.EmployeeManagementSystemAdvance.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeMapperTest {

    private EmployeeMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new EmployeeMapperImpl();
    }

    // ========== toEntity() Tests ==========
    
    @Test
    void testToEntity_WithValidData() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setFirstName("Pranjali");
        dto.setLastName("Sharma");
        dto.setDepartment("IT");
        dto.setSalary(5000.0);

        Employee entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals("Pranjali", entity.getFirstName());
        assertEquals("Sharma", entity.getLastName());
        assertEquals("IT", entity.getDepartment());
        assertEquals(5000.0, entity.getSalary());
        assertNull(entity.getId()); // ID should be null for new entities
    }

    @Test
    void testToEntity_WithNullDTO() {
        Employee entity = mapper.toEntity(null);
        assertNull(entity);
    }

    @Test
    void testToEntity_WithNullFields() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setFirstName(null);
        dto.setLastName(null);
        dto.setDepartment(null);
        dto.setSalary(0.0);

        Employee entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertNull(entity.getFirstName());
        assertNull(entity.getLastName());
        assertNull(entity.getDepartment());
        assertEquals(0.0, entity.getSalary());
    }

    @Test
    void testToEntity_WithEmptyStrings() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setFirstName("");
        dto.setLastName("");
        dto.setDepartment("");
        dto.setSalary(100.0);

        Employee entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals("", entity.getFirstName());
        assertEquals("", entity.getLastName());
        assertEquals("", entity.getDepartment());
        assertEquals(100.0, entity.getSalary());
    }

    @Test
    void testToEntity_WithZeroSalary() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setFirstName("Test");
        dto.setLastName("User");
        dto.setDepartment("HR");
        dto.setSalary(0.0);

        Employee entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(0.0, entity.getSalary());
    }

    @Test
    void testToEntity_WithNegativeSalary() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setFirstName("Test");
        dto.setLastName("User");
        dto.setDepartment("HR");
        dto.setSalary(-1000.0);

        Employee entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(-1000.0, entity.getSalary());
    }

    // ========== toResponseDTO() Tests ==========

    @Test
    void testToResponseDTO_WithValidData() {
        Employee entity = new Employee(1L, "Pranjali", "Sharma", "IT", 5000.0);

        EmployeeResponseDTO dto = mapper.toResponseDTO(entity);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Pranjali", dto.getFirstName());
        assertEquals("Sharma", dto.getLastName());
        assertEquals("IT", dto.getDepartment());
    }

    @Test
    void testToResponseDTO_WithNullEntity() {
        EmployeeResponseDTO dto = mapper.toResponseDTO(null);
        assertNull(dto);
    }

    @Test
    void testToResponseDTO_WithNullFields() {
        Employee entity = new Employee();
        entity.setId(1L);
        entity.setFirstName(null);
        entity.setLastName(null);
        entity.setDepartment(null);
        entity.setSalary(0.0);

        EmployeeResponseDTO dto = mapper.toResponseDTO(entity);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertNull(dto.getFirstName());
        assertNull(dto.getLastName());
        assertNull(dto.getDepartment());
    }

    @Test
    void testToResponseDTO_WithEmptyStrings() {
        Employee entity = new Employee(2L, "", "", "", 0.0);

        EmployeeResponseDTO dto = mapper.toResponseDTO(entity);

        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals("", dto.getFirstName());
        assertEquals("", dto.getLastName());
        assertEquals("", dto.getDepartment());
    }

    @Test
    void testToResponseDTO_DoesNotIncludeSalary() {
        Employee entity = new Employee(1L, "John", "Doe", "IT", 99999.0);

        EmployeeResponseDTO dto = mapper.toResponseDTO(entity);

        assertNotNull(dto);
        // Verify salary is not exposed in V1 response
        assertDoesNotThrow(() -> dto.getId());
    }

    // ========== toResponseV2DTO() Tests ==========

    @Test
    void testToResponseV2DTO_WithValidData() {
        Employee entity = new Employee(1L, "Pranjali", "Sharma", "IT", 5000.0);

        EmployeeResponseV2DTO dto = mapper.toResponseV2DTO(entity);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Pranjali", dto.getFirstName());
        assertEquals("Sharma", dto.getLastName());
        assertEquals("Pranjali Sharma", dto.getFullName());
        assertEquals("IT", dto.getDepartment());
        assertEquals(5000, dto.getSalary()); // Note: converted to int
    }

    @Test
    void testToResponseV2DTO_WithNullEntity() {
        EmployeeResponseV2DTO dto = mapper.toResponseV2DTO(null);
        assertNull(dto);
    }

    @Test
    void testToResponseV2DTO_WithNullFields() {
        Employee entity = new Employee();
        entity.setId(1L);
        entity.setFirstName(null);
        entity.setLastName(null);
        entity.setDepartment(null);
        entity.setSalary(0.0);

        EmployeeResponseV2DTO dto = mapper.toResponseV2DTO(entity);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertNull(dto.getFirstName());
        assertNull(dto.getLastName());
        assertNull(dto.getDepartment());
        assertEquals(0, dto.getSalary());
    }

    @Test
    void testToResponseV2DTO_WithEmptyStrings() {
        Employee entity = new Employee(2L, "", "", "", 1000.0);

        EmployeeResponseV2DTO dto = mapper.toResponseV2DTO(entity);

        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals("", dto.getFirstName());
        assertEquals("", dto.getLastName());
        assertEquals("", dto.getDepartment());
        assertEquals(1000, dto.getSalary());
    }

    @Test
    void testToResponseV2DTO_SalaryConversionDoubleToInt() {
        Employee entity = new Employee(1L, "Test", "User", "HR", 5000.99);

        EmployeeResponseV2DTO dto = mapper.toResponseV2DTO(entity);

        assertNotNull(dto);
        assertEquals(5000, dto.getSalary()); // Truncated to int
    }

    @Test
    void testToResponseV2DTO_WithZeroSalary() {
        Employee entity = new Employee(1L, "Test", "User", "IT", 0.0);

        EmployeeResponseV2DTO dto = mapper.toResponseV2DTO(entity);

        assertNotNull(dto);
        assertEquals(0, dto.getSalary());
    }

    @Test
    void testToResponseV2DTO_WithLargeSalary() {
        Employee entity = new Employee(1L, "Test", "User", "IT", 999999.99);

        EmployeeResponseV2DTO dto = mapper.toResponseV2DTO(entity);

        assertNotNull(dto);
        assertEquals(999999, dto.getSalary());
    }

    @Test
    void testToResponseV2DTO_MessageFieldInitiallyNull() {
        Employee entity = new Employee(1L, "Test", "User", "IT", 5000.0);

        EmployeeResponseV2DTO dto = mapper.toResponseV2DTO(entity);

        assertNotNull(dto);
        assertNull(dto.getMessage()); // Message should be null initially
    }

    // ========== Integration Tests ==========

    @Test
    void testFullCycle_RequestToEntityToResponse() {
        // Create request
        EmployeeRequestDTO requestDTO = new EmployeeRequestDTO();
        requestDTO.setFirstName("Alice");
        requestDTO.setLastName("Johnson");
        requestDTO.setDepartment("Finance");
        requestDTO.setSalary(75000.0);

        // Convert to entity
        Employee entity = mapper.toEntity(requestDTO);
        entity.setId(10L); // Simulate DB generated ID

        // Convert to V1 response
        EmployeeResponseDTO responseV1 = mapper.toResponseDTO(entity);
        assertEquals(10L, responseV1.getId());
        assertEquals("Alice", responseV1.getFirstName());
        assertEquals("Johnson", responseV1.getLastName());
        assertEquals("Finance", responseV1.getDepartment());

        // Convert to V2 response
        EmployeeResponseV2DTO responseV2 = mapper.toResponseV2DTO(entity);
        assertEquals(10L, responseV2.getId());
        assertEquals("Alice", responseV2.getFirstName());
        assertEquals("Johnson", responseV2.getLastName());
        assertEquals("Finance", responseV2.getDepartment());
        assertEquals(75000, responseV2.getSalary());
        assertEquals("Alice Johnson", responseV2.getFullName());
    }

    @Test
    void testMultipleMappings() {
        EmployeeRequestDTO dto1 = new EmployeeRequestDTO("John", "Doe", "IT", 5000.0);
        EmployeeRequestDTO dto2 = new EmployeeRequestDTO("Jane", "Smith", "HR", 6000.0);

        Employee entity1 = mapper.toEntity(dto1);
        Employee entity2 = mapper.toEntity(dto2);

        assertNotEquals(entity1.getFirstName(), entity2.getFirstName());
        assertNotEquals(entity1.getDepartment(), entity2.getDepartment());
    }
}