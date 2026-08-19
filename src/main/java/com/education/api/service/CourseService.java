package com.education.api.service;

import com.education.api.dto.request.CourseRequest;
import com.education.api.dto.response.CourseResponse;
import com.education.api.dto.response.StudentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {

    CourseResponse create(CourseRequest request);

    Page<CourseResponse> findAll(String title, Pageable pageable); // CHANGED: List -> Page + added title + Pageable

    CourseResponse findById(Long id);

    CourseResponse update(Long id, CourseRequest request);

    void delete(Long id);

    List<StudentResponse> findStudentsByCourseId(Long courseId);

    Page<CourseResponse> searchCoursesByName(String title, Pageable pageable);
    void testTransaction();
}