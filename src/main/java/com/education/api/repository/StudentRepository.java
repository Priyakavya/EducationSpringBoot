package com.education.api.repository;

import com.education.api.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /** Used by the duplicate-email check on create. */
    Optional<Student> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    /** Duplicate check on update: "does any OTHER row already own this email?" */
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    /** Optional ?name= filter on GET /api/students. */
    List<Student> findByNameContainingIgnoreCase(String name);

    /**
     * All students taking a given course.
     * JOIN FETCH is not needed here - we only read scalar student columns.
     */
    @Query("SELECT e.student FROM Enrollment e WHERE e.course.id = :courseId")
    List<Student> findAllByCourseId(@Param("courseId") Long courseId);
    Optional<Student> findByEmail(String email);
    
    Page<Student> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
}
