package com.education.api.service;

import com.education.api.dto.request.TeacherRequest;
import com.education.api.dto.response.CourseResponse;
import com.education.api.dto.response.TeacherResponse;

import java.util.List;

public interface TeacherService {

    TeacherResponse create(TeacherRequest request);

    List<TeacherResponse> findAll(String departmentFilter);

    TeacherResponse findById(Long id);

    TeacherResponse update(Long id, TeacherRequest request);

    void delete(Long id);

    /** Extra: GET /api/teachers/{id}/courses */
    List<CourseResponse> findCoursesByTeacherId(Long teacherId);
}
