package com.siakad.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test ini dibuat untuk memastikan semua class dalam package com.siakad.model
 * ter-cover oleh JaCoCo (getter, setter, dan konstruktor).
 * Tujuan: mencapai 100% line coverage untuk model entity.
 */
class ModelAllCoverageTest {

    @Test
    @DisplayName("Menguji semua model class agar ter-cover JaCoCo")
    void testAllModels() {
        // ========================
        // 🧩 STUDENT
        // ========================
        Student s = new Student("S001", "Arga", "arga@mail.com", "RKS", 3, 3.5, "ACTIVE");
        assertEquals("S001", s.getStudentId());
        assertEquals("Arga", s.getName());
        assertEquals("arga@mail.com", s.getEmail());
        assertEquals("RKS", s.getMajor());
        assertEquals(3, s.getSemester());
        assertEquals(3.5, s.getGpa());
        assertEquals("ACTIVE", s.getAcademicStatus());

        // Setter test lengkap
        s.setStudentId("S002");
        s.setName("Putra");
        s.setEmail("putra@mail.com");
        s.setMajor("Cyber");
        s.setSemester(5);
        s.setGpa(3.8);
        s.setAcademicStatus("PROBATION");

        assertEquals("S002", s.getStudentId());
        assertEquals("Putra", s.getName());
        assertEquals("putra@mail.com", s.getEmail());
        assertEquals("Cyber", s.getMajor());
        assertEquals(5, s.getSemester());
        assertEquals(3.8, s.getGpa());
        assertEquals("PROBATION", s.getAcademicStatus());

        assertNotNull(new Student()); // konstruktor kosong

        // ========================
        // 🧩 COURSE
        // ========================
        Course c = new Course("IF101", "Algoritma", 3, 40, 10, "Dr. Dosen");
        assertEquals("IF101", c.getCourseCode());
        assertEquals("Algoritma", c.getCourseName());
        assertEquals(3, c.getCredits());
        assertEquals(40, c.getCapacity());
        assertEquals(10, c.getEnrolledCount());
        assertEquals("Dr. Dosen", c.getLecturer());

        // Setter lengkap
        c.setCourseCode("IF999");
        c.setCourseName("Struktur Data");
        c.setCredits(5);
        c.setCapacity(50);
        c.setEnrolledCount(12);
        c.setLecturer("Prof. X");

        assertEquals("IF999", c.getCourseCode());
        assertEquals("Struktur Data", c.getCourseName());
        assertEquals(5, c.getCredits());
        assertEquals(50, c.getCapacity());
        assertEquals(12, c.getEnrolledCount());
        assertEquals("Prof. X", c.getLecturer());

        // Test addPrerequisite
        c.addPrerequisite("IF100");
        assertTrue(c.getPrerequisites().contains("IF100"));

        // Test konstruktor kosong dan kondisi prerequisites == null
        Course c2 = new Course();
        c2.setPrerequisites(null);
        c2.addPrerequisite("NEW");
        assertEquals("NEW", c2.getPrerequisites().get(0));

        assertNotNull(new Course());

        // ========================
        // 🧩 ENROLLMENT
        // ========================
        LocalDateTime now = LocalDateTime.now();
        Enrollment e = new Enrollment("E01", "S001", "IF101", now, "APPROVED");

        assertEquals("E01", e.getEnrollmentId());
        assertEquals("S001", e.getStudentId());
        assertEquals("IF101", e.getCourseCode());
        assertEquals("APPROVED", e.getStatus());
        assertEquals(now, e.getEnrollmentDate());

        // Setter lengkap
        e.setEnrollmentId("E02");
        e.setStudentId("S002");
        e.setCourseCode("IF202");
        e.setEnrollmentDate(LocalDateTime.of(2025, 1, 1, 0, 0));
        e.setStatus("REJECTED");

        assertEquals("E02", e.getEnrollmentId());
        assertEquals("S002", e.getStudentId());
        assertEquals("IF202", e.getCourseCode());
        assertEquals(LocalDateTime.of(2025, 1, 1, 0, 0), e.getEnrollmentDate());
        assertEquals("REJECTED", e.getStatus());

        assertNotNull(new Enrollment());

        // ========================
        // 🧩 COURSEGRADE
        // ========================
        CourseGrade g = new CourseGrade("IF101", 3, 4.0);
        assertEquals("IF101", g.getCourseCode());
        assertEquals(3, g.getCredits());
        assertEquals(4.0, g.getGradePoint());

        // Setter lengkap
        g.setCourseCode("IF102");
        g.setCredits(2);
        g.setGradePoint(3.5);

        assertEquals("IF102", g.getCourseCode());
        assertEquals(2, g.getCredits());
        assertEquals(3.5, g.getGradePoint());

        assertNotNull(new CourseGrade());
    }
}
