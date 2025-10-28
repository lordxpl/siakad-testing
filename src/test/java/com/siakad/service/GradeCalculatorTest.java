package com.siakad.service;

import com.siakad.model.CourseGrade;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GradeCalculatorTest {

    private final GradeCalculator gradeCalculator = new GradeCalculator();

    // ================================
    // 🧩 calculateGPA(List)
    // ================================
    @Test
    void testCalculateGPA_NormalCase() {
        List<CourseGrade> grades = Arrays.asList(
                new CourseGrade("CS101", 3, 4.0),
                new CourseGrade("CS102", 2, 3.0),
                new CourseGrade("CS103", 1, 2.0)
        );

        double gpa = gradeCalculator.calculateGPA(grades);
        assertEquals(3.33, gpa);
    }

    @Test
    void testCalculateGPA_EmptyList() {
        assertEquals(0.0, gradeCalculator.calculateGPA(Collections.emptyList()));
    }

    @Test
    void testCalculateGPA_NullList() {
        assertEquals(0.0, gradeCalculator.calculateGPA(null));
    }

    @Test
    void testCalculateGPA_InvalidGradePoint_TooHigh() {
        List<CourseGrade> grades = List.of(new CourseGrade("CS101", 3, 4.5));
        assertThrows(IllegalArgumentException.class, () -> gradeCalculator.calculateGPA(grades));
    }

    @Test
    void testCalculateGPA_InvalidGradePoint_Negative() {
        List<CourseGrade> grades = List.of(new CourseGrade("CS101", 3, -1.0));
        assertThrows(IllegalArgumentException.class, () -> gradeCalculator.calculateGPA(grades));
    }

    @Test
    void testCalculateGPA_ZeroCredits() {
        List<CourseGrade> grades = List.of(new CourseGrade("CS101", 0, 3.0));
        assertEquals(0.0, gradeCalculator.calculateGPA(grades));
    }

    // ================================
    // 🧩 determineAcademicStatus(double, int)
    // ================================
    @Test
    void testDetermineAcademicStatus_Semester1_Active() {
        assertEquals("ACTIVE", gradeCalculator.determineAcademicStatus(2.5, 1));
    }

    @Test
    void testDetermineAcademicStatus_Semester1_Probation() {
        assertEquals("PROBATION", gradeCalculator.determineAcademicStatus(1.8, 2));
    }

    @Test
    void testDetermineAcademicStatus_Semester3_Active_Boundary225() {
        assertEquals("ACTIVE", gradeCalculator.determineAcademicStatus(2.25, 3));
    }

    @Test
    void testDetermineAcademicStatus_Semester3_Probation() {
        assertEquals("PROBATION", gradeCalculator.determineAcademicStatus(2.1, 3));
    }

    @Test
    void testDetermineAcademicStatus_Semester3_Suspended() {
        assertEquals("SUSPENDED", gradeCalculator.determineAcademicStatus(1.5, 3));
    }

    @Test
    void testDetermineAcademicStatus_Semester5_Active_Boundary25() {
        assertEquals("ACTIVE", gradeCalculator.determineAcademicStatus(2.5, 5));
    }

    @Test
    void testDetermineAcademicStatus_Semester5_Probation() {
        assertEquals("PROBATION", gradeCalculator.determineAcademicStatus(2.3, 6));
    }

    @Test
    void testDetermineAcademicStatus_Semester5_Suspended() {
        assertEquals("SUSPENDED", gradeCalculator.determineAcademicStatus(1.9, 5));
    }

    @Test
    void testDetermineAcademicStatus_InvalidGPA_High() {
        assertThrows(IllegalArgumentException.class, () -> gradeCalculator.determineAcademicStatus(4.5, 3));
    }

    @Test
    void testDetermineAcademicStatus_InvalidGPA_Low() {
        assertThrows(IllegalArgumentException.class, () -> gradeCalculator.determineAcademicStatus(-0.5, 2));
    }

    @Test
    void testDetermineAcademicStatus_InvalidSemester() {
        assertThrows(IllegalArgumentException.class, () -> gradeCalculator.determineAcademicStatus(3.0, 0));
    }

    // ================================
    // 🧩 calculateMaxCredits(double)
    // ================================
    @Test
    void testCalculateMaxCredits_AllRanges() {
        assertEquals(24, gradeCalculator.calculateMaxCredits(3.5));
        assertEquals(21, gradeCalculator.calculateMaxCredits(2.7));
        assertEquals(18, gradeCalculator.calculateMaxCredits(2.1));
        assertEquals(15, gradeCalculator.calculateMaxCredits(1.5));
    }

    @Test
    void testCalculateMaxCredits_BoundariesAndInvalid() {
        assertEquals(15, gradeCalculator.calculateMaxCredits(0.0)); // batas bawah valid
        assertEquals(24, gradeCalculator.calculateMaxCredits(4.0)); // batas atas valid
        assertThrows(IllegalArgumentException.class, () -> gradeCalculator.calculateMaxCredits(-0.1)); // invalid bawah
        assertThrows(IllegalArgumentException.class, () -> gradeCalculator.calculateMaxCredits(4.5));  // invalid atas
    }
}
