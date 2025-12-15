package com.example.EmployeeManagementSystemAdvance.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ActiveProfiles;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.EmployeeManagementSystemAdvance.dto.EmployeeRequestDTO;
import com.example.EmployeeManagementSystemAdvance.dto.EmployeeResponseDTO;
import com.example.EmployeeManagementSystemAdvance.entity.Employee;
import com.example.EmployeeManagementSystemAdvance.exception.ResourceNotFoundException;
import com.example.EmployeeManagementSystemAdvance.repository.EmployeeRepository;
// tells Junit to use Mockito
//Mockito we use so ew can mock the repo
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class EmployeeServiceTest {
// fake object creation using @Mock
    @Mock
    private EmployeeRepository repo;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeService service;

    @BeforeEach
    void setUp() {
    	
       
    }

    @Test
    void testSaveEmployee_returnsResponseDto() {
       //input DTO for the test
    	//sent to service method
        EmployeeRequestDTO requestDto = new EmployeeRequestDTO();
        requestDto.setName("John");
        requestDto.setDepartment("IT");
       
// entity returned by modelMapper when mapping DTO->Entity
        // it converts dto to entity as repo works with entity only
        Employee mappedEntity = new Employee();
        mappedEntity.setName("John");
        mappedEntity.setDepartment("IT");
        
// entity returned by repo after saving
        Employee savedEntity = new Employee();
        savedEntity.setId(1L);
        savedEntity.setName("John");
        savedEntity.setDepartment("IT");
        
// This is the expected ResponseDTO that the service method should return
        EmployeeResponseDTO responseDto = new EmployeeResponseDTO();
        responseDto.setId(1L);
        responseDto.setName("John");
        responseDto.setDepartment("IT");
        

        //preparing mocks for the test I provide
        //help to test the service method without touching the real database
        when(modelMapper.map(requestDto, Employee.class)).thenReturn(mappedEntity);
        when(repo.save(mappedEntity)).thenReturn(savedEntity);
        when(modelMapper.map(savedEntity, EmployeeResponseDTO.class)).thenReturn(responseDto);

        
        EmployeeResponseDTO result = service.saveEmployee(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getName());
        verify(repo, times(1)).save(mappedEntity);
        verify(modelMapper).map(requestDto, Employee.class);
        verify(modelMapper).map(savedEntity, EmployeeResponseDTO.class);
    }

    @Test
    void testSaveEmployeeEntity_returnsEntity() {
        // Test saveEmployeeEntity which returns Employee entity
        EmployeeRequestDTO requestDto = new EmployeeRequestDTO();
        requestDto.setName("Alice");
        requestDto.setDepartment("HR");
    

        Employee mappedEntity = new Employee();
        mappedEntity.setName("Alice");
        mappedEntity.setDepartment("HR");
       

        Employee savedEntity = new Employee();
        savedEntity.setId(10L);
        savedEntity.setName("Alice");
        savedEntity.setDepartment("HR");
       

        when(modelMapper.map(requestDto, Employee.class)).thenReturn(mappedEntity);
        when(repo.save(mappedEntity)).thenReturn(savedEntity);

        Employee result = service.saveEmployeeEntity(requestDto);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Alice", result.getName());
        verify(repo).save(mappedEntity);
    }

    @Test
    void testGetAllEmployees_mapsToResponseDtoList() {
        Employee e1 = new Employee();
        e1.setId(1L);
        e1.setName("A");
        e1.setDepartment("D1");
        

        Employee e2 = new Employee();
        e2.setId(2L);
        e2.setName("B");
        e2.setDepartment("D2");
       

        when(repo.findAll()).thenReturn(Arrays.asList(e1, e2));

        // Mock mapping from each entity to response DTO
        EmployeeResponseDTO r1 = new EmployeeResponseDTO();
        r1.setId(1L);
        r1.setName("A");
        r1.setDepartment("D1");
      

        EmployeeResponseDTO r2 = new EmployeeResponseDTO();
        r2.setId(2L);
        r2.setName("B");
        r2.setDepartment("D2");
        

        when(modelMapper.map(e1, EmployeeResponseDTO.class)).thenReturn(r1);
        when(modelMapper.map(e2, EmployeeResponseDTO.class)).thenReturn(r2);

        List<EmployeeResponseDTO> result = service.getAllEmployees();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getName());
        assertEquals("B", result.get(1).getName());
        verify(repo).findAll();
    }

    @Test
    void testGetEmployeeById_found() {
        Employee emp = new Employee();
        //5 we want 
        emp.setId(5L);
        emp.setName("Found");

        when(repo.findById(5L)).thenReturn(Optional.of(emp));

        Optional<Employee> result = service.getEmployeeById(5L);

        assertTrue(result.isPresent());
        assertEquals("Found", result.get().getName());
        verify(repo).findById(5L);
    }

    @Test
    void testGetEmployeeById_notFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        Optional<Employee> result = service.getEmployeeById(99L);

        assertFalse(result.isPresent());
        verify(repo).findById(99L);
    }

    @Test
    void testUpdateEmployee_success() {
        Long id = 2L;

        // existing entity in repo
        Employee existing = new Employee();
        existing.setId(id);
        existing.setName("Old");
        existing.setDepartment("OldDept");
       

        // incoming DTO
        EmployeeRequestDTO updateDto = new EmployeeRequestDTO();
        updateDto.setName("New");
        updateDto.setDepartment("NewDept");
       

        // repository find and save flow
        when(repo.findById(id)).thenReturn(Optional.of(existing));

        // simulate modelMapper.map(dto, existing) to copy values into existing
        // use Answer to perform the copy on the passed instance
        Mockito.doAnswer(invocation -> {
            EmployeeRequestDTO src = invocation.getArgument(0);
            Employee dest = invocation.getArgument(1);
            // copy only fields used in service
            dest.setName(src.getName());
            dest.setDepartment(src.getDepartment());
            
            return dest;
        }).when(modelMapper).map(eq(updateDto), eq(existing));

        Employee updatedEntity = new Employee();
        updatedEntity.setId(id);
        updatedEntity.setName("New");
        updatedEntity.setDepartment("NewDept");
       

        when(repo.save(existing)).thenReturn(updatedEntity);

        EmployeeResponseDTO expectedResponse = new EmployeeResponseDTO();
        expectedResponse.setId(id);
        expectedResponse.setName("New");
        expectedResponse.setDepartment("NewDept");
        

        when(modelMapper.map(updatedEntity, EmployeeResponseDTO.class)).thenReturn(expectedResponse);

        // Act
        EmployeeResponseDTO resp = service.updateEmployee(id, updateDto);

        // Assert
        assertNotNull(resp);
        assertEquals("New", resp.getName());
        assertEquals(id, resp.getId());
        verify(repo).findById(id);
        verify(modelMapper).map(eq(updateDto), eq(existing));
        verify(repo).save(existing);
    }

    @Test
    void testUpdateEmployee_notFound_throws() {
        Long id = 99L;
        EmployeeRequestDTO updateDto = new EmployeeRequestDTO();
        updateDto.setName("Whatever");

        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.updateEmployee(id, updateDto));
        verify(repo).findById(id);
        verify(repo, never()).save(any());
    }

    @Test
    void testDeleteEmployee_success() {
        Long id = 7L;
        Employee existing = new Employee();
        existing.setId(id);
        existing.setName("ToDelete");

        when(repo.findById(id)).thenReturn(Optional.of(existing));
        // repository.delete(employee) is void; no need to stub

        service.deleteEmployee(id);

        verify(repo).findById(id);
        verify(repo).delete(existing);
    }

    @Test
    void testDeleteEmployee_notFound_throws() {
        Long id = 77L;
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.deleteEmployee(id));
        verify(repo).findById(id);
        verify(repo, never()).delete(any());
    }
}
