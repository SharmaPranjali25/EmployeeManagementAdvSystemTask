package com.example.EmployeeManagementSystemAdvance.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleResourceNotFoundException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");

        ResponseEntity<Map<String, Object>> response = handler.handleResourceNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(404, body.get("status"));
        assertEquals("Resource Not Found", body.get("error"));
        assertEquals("Resource not found", body.get("message"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handleDuplicateResourceException() {
        DuplicateResourceException exception = new DuplicateResourceException("Duplicate resource");

        ResponseEntity<Map<String, Object>> response = handler.handleDuplicateResourceException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(409, body.get("status"));
        assertEquals("Duplicate Resource", body.get("error"));
        assertEquals("Duplicate resource", body.get("message"));
    }

    @Test
    void handleIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");

        ResponseEntity<Map<String, Object>> response = handler.handleIllegalArgumentException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.get("status"));
        assertEquals("Invalid Argument", body.get("error"));
        assertEquals("Invalid argument", body.get("message"));
    }

    @Test
    void handleIllegalStateException() {
        IllegalStateException exception = new IllegalStateException("Invalid state");

        ResponseEntity<Map<String, Object>> response = handler.handleIllegalStateException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.get("status"));
        assertEquals("Invalid State", body.get("error"));
        assertEquals("Invalid state", body.get("message"));
    }

    @Test
    void handleRuntimeException() {
        RuntimeException exception = new RuntimeException("Runtime error");

        ResponseEntity<Map<String, Object>> response = handler.handleRuntimeException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(500, body.get("status"));
        assertEquals("Internal Server Error", body.get("error"));
        assertEquals("Runtime error", body.get("message"));
    }

    @Test
    void handleGenericException() {
        Exception exception = new Exception("Generic error");

        ResponseEntity<Map<String, Object>> response = handler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(500, body.get("status"));
        assertEquals("Internal Server Error", body.get("error"));
        assertEquals("An unexpected error occurred", body.get("message"));
    }
}