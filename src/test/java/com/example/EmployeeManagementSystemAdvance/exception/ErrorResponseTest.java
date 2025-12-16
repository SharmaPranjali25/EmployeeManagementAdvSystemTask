package com.example.EmployeeManagementSystemAdvance.exception;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void testNoArgsConstructor() {
        ErrorResponse response = new ErrorResponse();
        assertNotNull(response);
        assertNull(response.getTimestamp());
        assertEquals(0, response.getStatus());
        assertNull(response.getError());
        assertNull(response.getMessage());
        assertNull(response.getPath());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        ErrorResponse response = new ErrorResponse(
                now,
                404,
                "Not Found",
                "Resource not found",
                "/api/employees/1"
        );

        assertEquals(now, response.getTimestamp());
        assertEquals(404, response.getStatus());
        assertEquals("Not Found", response.getError());
        assertEquals("Resource not found", response.getMessage());
        assertEquals("/api/employees/1", response.getPath());
    }

    @Test
    void testSettersAndGetters() {
        ErrorResponse response = new ErrorResponse();
        LocalDateTime now = LocalDateTime.now();

        response.setTimestamp(now);
        response.setStatus(500);
        response.setError("Internal Server Error");
        response.setMessage("Something went wrong");
        response.setPath("/api/employees");

        assertEquals(now, response.getTimestamp());
        assertEquals(500, response.getStatus());
        assertEquals("Internal Server Error", response.getError());
        assertEquals("Something went wrong", response.getMessage());
        assertEquals("/api/employees", response.getPath());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();

        ErrorResponse response1 = new ErrorResponse(
                now, 404, "Not Found", "Resource not found", "/api/employees/1"
        );

        ErrorResponse response2 = new ErrorResponse(
                now, 404, "Not Found", "Resource not found", "/api/employees/1"
        );

        ErrorResponse response3 = new ErrorResponse(
                now, 500, "Server Error", "Error occurred", "/api/employees/2"
        );

        // Test equals
        assertEquals(response1, response2);
        assertNotEquals(response1, response3);

        // Test reflexivity
        assertEquals(response1, response1);

        // Test symmetry
        assertEquals(response2, response1);

        // Test null comparison
        assertNotEquals(null, response1);

        // Test hashCode
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testToString() {
        LocalDateTime now = LocalDateTime.now();
        ErrorResponse response = new ErrorResponse(
                now, 404, "Not Found", "Resource not found", "/api/employees/1"
        );

        String toString = response.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("404"));
        assertTrue(toString.contains("Not Found"));
        assertTrue(toString.contains("Resource not found"));
        assertTrue(toString.contains("/api/employees/1"));
    }

    @Test
    void testEqualsWithDifferentClass() {
        LocalDateTime now = LocalDateTime.now();
        ErrorResponse response = new ErrorResponse(
                now, 404, "Not Found", "Resource not found", "/api/employees/1"
        );

        assertNotEquals(response, "Some String");
    }

    @Test
    void testWithNullValues() {
        ErrorResponse error = new ErrorResponse(null, 0, null, null, null);

        assertNull(error.getTimestamp());
        assertEquals(0, error.getStatus());
        assertNull(error.getError());
        assertNull(error.getMessage());
        assertNull(error.getPath());
    }

    @Test
    void testWithEmptyStrings() {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                500,
                "",
                "",
                ""
        );

        assertEquals(500, error.getStatus());
        assertEquals("", error.getError());
        assertEquals("", error.getMessage());
        assertEquals("", error.getPath());
    }

    @Test
    void testHashCodeConsistency() {
        LocalDateTime now = LocalDateTime.now();
        ErrorResponse error = new ErrorResponse(now, 404, "Not Found", "Test", "/api");

        int hash1 = error.hashCode();
        int hash2 = error.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void testWithDifferentStatusCodes() {
        ErrorResponse error400 = new ErrorResponse(null, 400, "Bad Request", "Invalid input", "/api");
        ErrorResponse error401 = new ErrorResponse(null, 401, "Unauthorized", "Auth failed", "/api");
        ErrorResponse error403 = new ErrorResponse(null, 403, "Forbidden", "Access denied", "/api");
        ErrorResponse error404 = new ErrorResponse(null, 404, "Not Found", "Resource not found", "/api");
        ErrorResponse error409 = new ErrorResponse(null, 409, "Conflict", "Duplicate", "/api");
        ErrorResponse error500 = new ErrorResponse(null, 500, "Internal Server Error", "Server error", "/api");

        assertEquals(400, error400.getStatus());
        assertEquals(401, error401.getStatus());
        assertEquals(403, error403.getStatus());
        assertEquals(404, error404.getStatus());
        assertEquals(409, error409.getStatus());
        assertEquals(500, error500.getStatus());
    }
}