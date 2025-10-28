package com.siakad.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test ini dibuat untuk memenuhi target coverage JaCoCo
 * dengan memastikan semua konstruktor Exception di package com.siakad.exception
 * dapat dijalankan tanpa error.
 */
class ExceptionAllCoverageTest {

    @Test
    @DisplayName("Mencakup semua konstruktor untuk kelas Exception di package exception")
    void coverAllExceptionClasses() {
        String errorMessage = "MessageError Uji";
        Throwable cause = new RuntimeException("Sebab Pengujian");

        // --- CourseFullException ---
        assertDoesNotThrow(() -> new CourseFullException(errorMessage));
        assertDoesNotThrow(() -> new CourseFullException(errorMessage, cause));

        // --- CourseNotFoundException ---
        assertDoesNotThrow(() -> new CourseNotFoundException(errorMessage));
        assertDoesNotThrow(() -> new CourseNotFoundException(errorMessage, cause));

        // --- EnrollmentException ---
        assertDoesNotThrow(() -> new EnrollmentException(errorMessage));
        assertDoesNotThrow(() -> new EnrollmentException(errorMessage, cause));

        // --- PrerequisiteNotMetException ---
        assertDoesNotThrow(() -> new PrerequisiteNotMetException(errorMessage));
        assertDoesNotThrow(() -> new PrerequisiteNotMetException(errorMessage, cause));

        // --- StudentNotFoundException ---
        assertDoesNotThrow(() -> new StudentNotFoundException(errorMessage));
        assertDoesNotThrow(() -> new StudentNotFoundException(errorMessage, cause));
    }
}
