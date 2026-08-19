package com.education.api;

import com.education.api.dto.request.GradeRequest;
import com.education.api.entity.GradeStatus;
import com.education.api.exception.BusinessRuleException;
import com.education.api.exception.DuplicateResourceException;
import com.education.api.service.GradeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Service-layer tests for the two rules that protect grade integrity.
 * These run against the seeded H2 data:
 *   enrollment 1 = student 1 in course 1  (exams 1 and 2 belong to course 1)
 *   exam 3       = DBMS Mid-Term, course 2
 */
@SpringBootTest
@ActiveProfiles("dev")
@Transactional
class GradeBusinessRuleTest {

    @Autowired
    private GradeService gradeService;

    @Test
    void recordingGradeForExamOnAnotherCourse_throwsBusinessRule() {
        // enrollment 1 is for course 1; exam 3 belongs to course 2.
        GradeRequest request = new GradeRequest(1L, 3L, new BigDecimal("75.00"),
        		GradeStatus.PASS);

        BusinessRuleException ex = assertThrows(
                BusinessRuleException.class, () -> gradeService.create(request));

        assertTrue(ex.getMessage().contains("belongs to course"));
    }

    @Test
    void recordingSecondGradeForSameEnrollmentAndExam_throwsDuplicate() {
        // Grade id 1 already covers enrollment 1 + exam 1 in the seed data.
        GradeRequest request = new GradeRequest(1L, 1L, new BigDecimal("60.00"),
        		GradeStatus.PASS);

        assertThrows(DuplicateResourceException.class, () -> gradeService.create(request));
    }

    @Test
    void averageForExamWithNoGrades_returnsZeroNotNull() {
        // Exam 8 (Web Development Final) has no grades in the seed data.
        Double average = gradeService.findAverageScoreByExamId(8L);

        assertNotNull(average);
        assertEquals(0.0, average);
    }

    @Test
    void letterGradeIsDerivedFromScore() {
        // enrollment 14 = student 7 in course 2; exam 4 is the DBMS Final.
        GradeRequest request = new GradeRequest(14L, 4L, new BigDecimal("91.00"),GradeStatus.PASS);

        assertEquals("A", gradeService.create(request).getLetterGrade());
    }
}
