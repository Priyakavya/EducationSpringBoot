package com.education.api.repository;

import com.education.api.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    List<Exam> findByCourseId(Long courseId);

    /** Optional ?from=&to= window on GET /api/exams. */
    List<Exam> findByExamDateBetween(LocalDate from, LocalDate to);

    @Query("SELECT e FROM Exam e JOIN FETCH e.course")
    List<Exam> findAllWithCourse();

    @Query("SELECT e FROM Exam e JOIN FETCH e.course WHERE e.id = :id")
    Optional<Exam> findByIdWithCourse(Long id);
}
