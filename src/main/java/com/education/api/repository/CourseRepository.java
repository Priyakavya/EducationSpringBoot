package com.education.api.repository;

import com.education.api.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByTeacherId(Long teacherId);

    List<Course> findByTitleContainingIgnoreCase(String title);

    boolean existsByTeacherId(Long teacherId);

    /**
     * JOIN FETCH loads the teacher in the SAME query.
     * Without it, listing 50 courses fires 1 + 50 queries (the N+1 problem)
     * because CourseMapper reads course.getTeacher().getName().
     */
    @Query("SELECT c FROM Course c JOIN FETCH c.teacher")
    List<Course> findAllWithTeacher();

    @Query("SELECT c FROM Course c JOIN FETCH c.teacher WHERE c.id = :id")
    Optional<Course> findByIdWithTeacher(Long id);
   
    Page<Course> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
