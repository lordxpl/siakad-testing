package com.siakad.service;

import com.siakad.exception.*;
import com.siakad.model.Course;
import com.siakad.model.Enrollment;
import com.siakad.model.Student;
import com.siakad.repository.CourseRepository;
import com.siakad.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Kelas ini menguji perilaku EnrollmentService menggunakan pendekatan Mock.
 * Semua dependensi eksternal (repository, service) disimulasikan agar pengujian fokus ke logika utama.
 **/
@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private NotificationService notificationService;
    @Mock
    private GradeCalculator gradeCalculator;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private Student testStudent;
    private Course testCourse;

    @BeforeEach
    void init() {
        testStudent = new Student("S001", "Aliffian", "aliffian@pnc.ac.id", "Rekayasa Keamanan Siber", 5, 3.75, "ACTIVE");
        testCourse = new Course("CS202", "Keamanan Jaringan", 3, 30, 10, "Dr. Riko Iman Decamarta");
    }

    // -------------------------------------------------------------
    // TEST CASE: ENROLL COURSE
    // -------------------------------------------------------------

    @Test
    @DisplayName("Enroll Course - Berhasil mendaftar ke mata kuliah")
    void testEnrollCourse_Success() {
        when(studentRepository.findById("S001")).thenReturn(testStudent);
        when(courseRepository.findByCourseCode("CS202")).thenReturn(testCourse);
        when(courseRepository.isPrerequisiteMet("S001", "CS202")).thenReturn(true);

        Enrollment result = enrollmentService.enrollCourse("S001", "CS202");

        assertNotNull(result);
        assertEquals("APPROVED", result.getStatus());
        assertEquals("S001", result.getStudentId());
        assertEquals(11, testCourse.getEnrolledCount());

        verify(courseRepository).update(testCourse);
        verify(notificationService).sendEmail(eq("aliffian@pnc.ac.id"), anyString(), contains("Keamanan Jaringan"));
    }

    @Test
    @DisplayName("Enroll Course - Mahasiswa tidak ditemukan")
    void testEnrollCourse_StudentNotFound() {
        when(studentRepository.findById("S999")).thenReturn(null);
        assertThrows(StudentNotFoundException.class, () -> enrollmentService.enrollCourse("S999", "CS202"));
    }

    @Test
    @DisplayName("Enroll Course - Mahasiswa berstatus SUSPENDED")
    void testEnrollCourse_StudentSuspended() {
        testStudent.setAcademicStatus("SUSPENDED");
        when(studentRepository.findById("S001")).thenReturn(testStudent);
        assertThrows(EnrollmentException.class, () -> enrollmentService.enrollCourse("S001", "CS202"));
    }

    @Test
    @DisplayName("Enroll Course - Mata kuliah tidak ditemukan")
    void testEnrollCourse_CourseNotFound() {
        when(studentRepository.findById("S001")).thenReturn(testStudent);
        when(courseRepository.findByCourseCode("CS999")).thenReturn(null);
        assertThrows(CourseNotFoundException.class, () -> enrollmentService.enrollCourse("S001", "CS999"));
    }

    @Test
    @DisplayName("Enroll Course - Kapasitas kelas penuh")
    void testEnrollCourse_CourseFull() {
        testCourse.setEnrolledCount(30);
        when(studentRepository.findById("S001")).thenReturn(testStudent);
        when(courseRepository.findByCourseCode("CS202")).thenReturn(testCourse);
        assertThrows(CourseFullException.class, () -> enrollmentService.enrollCourse("S001", "CS202"));
    }

    @Test
    @DisplayName("Enroll Course - Prasyarat belum terpenuhi")
    void testEnrollCourse_PrerequisiteNotMet() {
        when(studentRepository.findById("S001")).thenReturn(testStudent);
        when(courseRepository.findByCourseCode("CS202")).thenReturn(testCourse);
        when(courseRepository.isPrerequisiteMet("S001", "CS202")).thenReturn(false);
        assertThrows(PrerequisiteNotMetException.class, () -> enrollmentService.enrollCourse("S001", "CS202"));
    }

    // -------------------------------------------------------------
    // TEST CASE: VALIDATE CREDIT LIMIT
    // -------------------------------------------------------------

    @Test
    @DisplayName("Validate Credit - SKS masih dalam batas maksimum")
    void testValidateCreditLimit_WithinLimit() {
        when(studentRepository.findById("S001")).thenReturn(testStudent);
        when(gradeCalculator.calculateMaxCredits(3.75)).thenReturn(24);
        assertTrue(enrollmentService.validateCreditLimit("S001", 22));
    }

    @Test
    @DisplayName("Validate Credit - SKS melebihi batas maksimum")
    void testValidateCreditLimit_ExceedsLimit() {
        when(studentRepository.findById("S001")).thenReturn(testStudent);
        when(gradeCalculator.calculateMaxCredits(3.75)).thenReturn(24);
        assertFalse(enrollmentService.validateCreditLimit("S001", 28));
    }

    @Test
    @DisplayName("Validate Credit - Mahasiswa tidak ditemukan")
    void testValidateCreditLimit_StudentNotFound() {
        when(studentRepository.findById("S404")).thenReturn(null);
        assertThrows(StudentNotFoundException.class, () -> enrollmentService.validateCreditLimit("S404", 18));
    }

    // -------------------------------------------------------------
    // TEST CASE: DROP COURSE
    // -------------------------------------------------------------

    @Test
    @DisplayName("Drop Course - Berhasil membatalkan mata kuliah")
    void testDropCourse_Success() {
        when(studentRepository.findById("S001")).thenReturn(testStudent);
        when(courseRepository.findByCourseCode("CS202")).thenReturn(testCourse);

        enrollmentService.dropCourse("S001", "CS202");

        assertEquals(9, testCourse.getEnrolledCount());
        verify(courseRepository).update(testCourse);
        verify(notificationService).sendEmail(eq("aliffian@pnc.ac.id"), anyString(), contains("dropped"));
    }

    @Test
    @DisplayName("Drop Course - Mahasiswa tidak ditemukan")
    void testDropCourse_StudentNotFound() {
        when(studentRepository.findById("S888")).thenReturn(null);
        assertThrows(StudentNotFoundException.class, () -> enrollmentService.dropCourse("S888", "CS202"));
    }

    @Test
    @DisplayName("Drop Course - Mata kuliah tidak ditemukan")
    void testDropCourse_CourseNotFound() {
        when(studentRepository.findById("S001")).thenReturn(testStudent);
        when(courseRepository.findByCourseCode("CS999")).thenReturn(null);
        assertThrows(CourseNotFoundException.class, () -> enrollmentService.dropCourse("S001", "CS999"));
    }
}
