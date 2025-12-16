package com.example.EmployeeManagementSystemAdvance.exception;

import com.example.EmployeeManagementSystemAdvance.dto.EmployeeRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.core.MethodParameter;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    // ---------------- VALIDATION ERROR ----------------
    @Test
    void handleValidationErrors_shouldReturnBadRequest() throws NoSuchMethodException {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(dto, "employee");

        bindingResult.rejectValue("firstName", "error.code", "First name is required");

        MethodParameter parameter = new MethodParameter(
                GlobalExceptionHandlerTest.class
                        .getDeclaredMethod("dummyMethod", EmployeeRequestDTO.class),
                0
        );

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(parameter, bindingResult);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/employees/add");

        ResponseEntity<ErrorResponse> response =
                handler.handleValidationErrors(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertTrue(response.getBody().getMessage().contains("firstName: First name is required"));
        assertEquals("/api/v1/employees/add", response.getBody().getPath());
        assertEquals("Bad Request", response.getBody().getError());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleValidationErrors_withMultipleFields_shouldConcatenateMessages()
            throws NoSuchMethodException {

        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(dto, "employee");

        bindingResult.addError(new FieldError("employee", "firstName", "First name is required"));
        bindingResult.addError(new FieldError("employee", "lastName", "Last name is required"));
        bindingResult.addError(new FieldError("employee", "salary", "Salary must be positive"));

        MethodParameter parameter = new MethodParameter(
                GlobalExceptionHandlerTest.class
                        .getDeclaredMethod("dummyMethod", EmployeeRequestDTO.class),
                0
        );

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(parameter, bindingResult);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/employees/add");

        ResponseEntity<ErrorResponse> response =
                handler.handleValidationErrors(ex, request);

        String message = response.getBody().getMessage();
        assertTrue(message.contains("firstName: First name is required"));
        assertTrue(message.contains("lastName: Last name is required"));
        assertTrue(message.contains("salary: Salary must be positive"));
    }

    // Dummy method required for MethodParameter
    public void dummyMethod(EmployeeRequestDTO dto) {}

    // ---------------- RESOURCE NOT FOUND ----------------
    @Test
    void handleResourceNotFound_shouldReturnNotFound() {
        ResourceNotFoundException ex =
                new ResourceNotFoundException("Employee not found");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/employees/1");

        ResponseEntity<ErrorResponse> response =
                handler.handleResourceNotFound(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Not Found", response.getBody().getError());
        assertEquals("Employee not found", response.getBody().getMessage());
        assertEquals("/api/v1/employees/1", response.getBody().getPath());
        assertNotNull(response.getBody().getTimestamp());
    }

    // ---------------- DUPLICATE RESOURCE ----------------
    @Test
    void handleDuplicateResource_shouldReturnConflict() {
        DuplicateResourceException ex =
                new DuplicateResourceException("Employee already exists");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/employees/add");

        ResponseEntity<ErrorResponse> response =
                handler.handleDuplicateResource(ex, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(409, response.getBody().getStatus());
        assertEquals("Conflict", response.getBody().getError());
        assertEquals("Employee already exists", response.getBody().getMessage());
        assertEquals("/api/v1/employees/add", response.getBody().getPath());
        assertNotNull(response.getBody().getTimestamp());
    }

    // ---------------- RUNTIME EXCEPTION ----------------
    @Test
    void handleRuntimeException_shouldReturnInternalServerError() {
        RuntimeException ex = new RuntimeException("Database connection failed");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/employees/all");

        ResponseEntity<ErrorResponse> response =
                handler.handleRuntimeException(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("Internal Server Error", response.getBody().getError());
        assertEquals("Database connection failed", response.getBody().getMessage());
        assertEquals("/api/v1/employees/all", response.getBody().getPath());
        assertNotNull(response.getBody().getTimestamp());
    }

    // ---------------- GLOBAL EXCEPTION ----------------
    @Test
    void handleGlobalException_shouldReturnInternalServerError() {
        Exception ex = new Exception("Something went wrong");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/employees/all");

        ResponseEntity<ErrorResponse> response =
                handler.handleGlobalException(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("Internal Server Error", response.getBody().getError());
        assertTrue(response.getBody().getMessage().startsWith("An unexpected error occurred:"));
        assertTrue(response.getBody().getMessage().contains("Something went wrong"));
        assertEquals("/api/v1/employees/all", response.getBody().getPath());
        assertNotNull(response.getBody().getTimestamp());
    }
}