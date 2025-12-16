package com.example.EmployeeManagementSystemAdvance.exception;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class DuplicateResourceExceptionTest {

    @Test
    void testExceptionMessage() {
        DuplicateResourceException ex = new DuplicateResourceException("Employee already exists");
        assertEquals("Employee already exists", ex.getMessage());
        assertNotNull(ex);
    }

    @Test
    void testExceptionWithDetailedMessage() {
        String message = "Duplicate data alert cannot save";
        DuplicateResourceException ex = new DuplicateResourceException(message);
        assertEquals(message, ex.getMessage());
    }

    @Test
    void testExceptionIsRuntimeException() {
        DuplicateResourceException ex = new DuplicateResourceException("Duplicate");
        assertInstanceOf(RuntimeException.class, ex);
        assertInstanceOf(Exception.class, ex);
    }

    @Test
    void testExceptionCanBeThrown() {
        DuplicateResourceException thrown = assertThrows(
                DuplicateResourceException.class,
                () -> {
                    throw new DuplicateResourceException("Test exception");
                }
        );
        assertEquals("Test exception", thrown.getMessage());
    }

    @Test
    void testExceptionWithNullMessage() {
        DuplicateResourceException ex = new DuplicateResourceException(null);
        assertNull(ex.getMessage());
        assertNotNull(ex);
    }

    @Test
    void testExceptionWithEmptyMessage() {
        DuplicateResourceException ex = new DuplicateResourceException("");
        assertEquals("", ex.getMessage());
        assertNotNull(ex);
    }

    @Test
    void testExceptionWithLongMessage() {
        String longMessage = "A".repeat(500);
        DuplicateResourceException ex = new DuplicateResourceException(longMessage);
        assertEquals(longMessage, ex.getMessage());
    }

    @Test
    void testExceptionCanBeCaught() {
        try {
            throw new DuplicateResourceException("Duplicate found");
        } catch (DuplicateResourceException e) {
            assertEquals("Duplicate found", e.getMessage());
        }
    }

    @Test
    void testExceptionCanBeCaughtAsRuntimeException() {
        try {
            throw new DuplicateResourceException("Test");
        } catch (RuntimeException e) {
            assertInstanceOf(DuplicateResourceException.class, e);
        }
    }

    @Test
    void testExceptionStackTrace() {
        DuplicateResourceException ex = new DuplicateResourceException("Test");
        assertNotNull(ex.getStackTrace());
        assertTrue(ex.getStackTrace().length > 0);
    }

    @Test
    void testExceptionToString() {
        DuplicateResourceException ex = new DuplicateResourceException("Duplicate data");
        String str = ex.toString();
        assertTrue(str.contains("DuplicateResourceException"));
        assertTrue(str.contains("Duplicate data"));
    }

    @Test
    void testExceptionWithSpecialCharacters() {
        String message = "Employee 'John O'Connor' already exists in department \"IT\"";
        DuplicateResourceException ex = new DuplicateResourceException(message);
        assertEquals(message, ex.getMessage());
    }

    @Test
    void testExceptionCause() {
        DuplicateResourceException ex = new DuplicateResourceException("Test");
        assertNull(ex.getCause());
    }

    @Test
    void testMultipleInstances() {
        DuplicateResourceException ex1 = new DuplicateResourceException("Error 1");
        DuplicateResourceException ex2 = new DuplicateResourceException("Error 2");

        assertNotEquals(ex1.getMessage(), ex2.getMessage());
        assertEquals("Error 1", ex1.getMessage());
        assertEquals("Error 2", ex2.getMessage());
        assertNotSame(ex1, ex2);
    }
}