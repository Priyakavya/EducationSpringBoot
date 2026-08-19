package com.education.api.repository;

import com.education.api.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    /** One score per enrolment per exam - enforced in code AND by a UNIQUE key. */
    boolean existsByEnrollmentIdAndExamId(Long enrollmentId, Long examId);

    boolean existsByEnrollmentIdAndExamIdAndIdNot(Long enrollmentId, Long examId, Long id);

    List<Grade> findByEnrollmentId(Long enrollmentId);

    List<Grade> findByExamId(Long examId);

    /** All grades for one student, across every course they take. */
    @Query("SELECT g FROM Grade g WHERE g.enrollment.student.id = :studentId")
    List<Grade> findAllByStudentId(@Param("studentId") Long studentId);

    /** Class average for an exam - returns null when the exam has no grades yet. */
    @Query("SELECT AVG(g.score) FROM Grade g WHERE g.exam.id = :examId")
    Double findAverageScoreByExamId(@Param("examId") Long examId);

    @Query("SELECT g FROM Grade g JOIN FETCH g.enrollment en JOIN FETCH en.student "
            + "JOIN FETCH en.course JOIN FETCH g.exam")
    List<Grade> findAllWithDetails();

    @Query("SELECT g FROM Grade g JOIN FETCH g.enrollment en JOIN FETCH en.student "
            + "JOIN FETCH en.course JOIN FETCH g.exam WHERE g.id = :id")
    Optional<Grade> findByIdWithDetails(Long id);
    
    @Query(value = "SELECT * FROM grades WHERE score >= ?1 ORDER BY score DESC LIMIT 10", nativeQuery = true)
    List<Grade> findTop10ByScoreNative(Double minScore);
}