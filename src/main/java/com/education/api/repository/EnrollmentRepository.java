package com.education.api.repository;

import com.education.api.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    /** Backs the "student already enrolled in this course" rule (409 Conflict). */
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    /** Same rule on update - ignore the row being edited. */
    boolean existsByStudentIdAndCourseIdAndIdNot(Long studentId, Long courseId, Long id);

    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByCourseId(Long courseId);

    boolean existsByCourseId(Long courseId);

    /** One query instead of 1 + 2N - the mapper needs both student and course. */
    @Query("SELECT e FROM Enrollment e JOIN FETCH e.student JOIN FETCH e.course")
    List<Enrollment> findAllWithDetails();

    @Query("SELECT e FROM Enrollment e JOIN FETCH e.student JOIN FETCH e.course WHERE e.id = :id")
    Optional<Enrollment> findByIdWithDetails(Long id);
}
