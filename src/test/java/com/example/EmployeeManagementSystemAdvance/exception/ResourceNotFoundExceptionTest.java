package com.example.EmployeeManagementSystemAdvance.exception;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ResourceNotFoundExceptionTest {

    @Test
    void testExceptionMessage() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Not found");
        assertEquals("Not found", ex.getMessage());
        assertNotNull(ex);
    }

    @Test
    void testExceptionWithDetailedMessage() {
        String message = "Employee with ID 123 not found in the database";
        ResourceNotFoundException ex = new ResourceNotFoundException(message);
        assertEquals(message, ex.getMessage());
        assertNotNull(ex);
    }

    @Test
    void testExceptionIsRuntimeException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Test");
        assertInstanceOf(RuntimeException.class, ex);
        assertInstanceOf(Exception.class, ex);
    }

    @Test
    void testExceptionCanBeThrown() {
        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> {
                    throw new ResourceNotFoundException("Resource not available");
                }
        );
        assertEquals("Resource not available", thrown.getMessage());
    }

    @Test
    void testExceptionWithNullMessage() {
        ResourceNotFoundException ex = new ResourceNotFoundException(null);
        assertNull(ex.getMessage());
        assertNotNull(ex);
    }

    @Test
    void testExceptionWithEmptyMessage() {
        ResourceNotFoundException ex = new ResourceNotFoundException("");
        assertEquals("", ex.getMessage());
        assertNotNull(ex);
    }

    @Test
    void testMultipleInstances() {
        ResourceNotFoundException ex1 = new ResourceNotFoundException("Error 1");
        ResourceNotFoundException ex2 = new ResourceNotFoundException("Error 2");

        assertNotEquals(ex1.getMessage(), ex2.getMessage());
        assertEquals("Error 1", ex1.getMessage());
        assertEquals("Error 2", ex2.getMessage());
        assertNotSame(ex1, ex2);
    }

    @Test
    void testExceptionStackTrace() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Test exception");
        assertNotNull(ex.getStackTrace());
        assertTrue(ex.getStackTrace().length > 0);
    }

    @Test
    void testExceptionCause() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Test");
        assertNull(ex.getCause());
    }

    @Test
    void testExceptionWithLongMessage() {
        String longMessage = "B".repeat(500);
        ResourceNotFoundException ex = new ResourceNotFoundException(longMessage);
        assertEquals(longMessage, ex.getMessage());
    }

    @Test
    void testExceptionCanBeCaught() {
        try {
            throw new ResourceNotFoundException("Not found");
        } catch (ResourceNotFoundException e) {
            assertEquals("Not found", e.getMessage());
        }
    }

    @Test
    void testExceptionCanBeCaughtAsRuntimeException() {
        try {
            throw new ResourceNotFoundException("Test");
        } catch (RuntimeException e) {
            assertInstanceOf(ResourceNotFoundException.class, e);
        }
    }

    @Test
    void testExceptionToString() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");
        String str = ex.toString();
        assertTrue(str.contains("ResourceNotFoundException"));
        assertTrue(str.contains("Resource not found"));
    }

    @Test
    void testExceptionWithIdInMessage() {
        String message = "Employee not found with id: 12345";
        ResourceNotFoundException ex = new ResourceNotFoundException(message);
        assertEquals(message, ex.getMessage());
        assertTrue(ex.getMessage().contains("12345"));
    }

    @Test
    void testExceptionWithSpecialCharacters() {
        String message = "Employee 'John O'Connor' not found in department \"IT\"";
        ResourceNotFoundException ex = new ResourceNotFoundException(message);
        assertEquals(message, ex.getMessage());
    }
}