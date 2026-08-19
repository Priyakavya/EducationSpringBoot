package com.education.api.mapper;

import com.education.api.dto.response.CourseResponse;
import com.education.api.entity.Course;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * No toEntity(request) here: building a Course needs a Teacher looked up from
 * the database, which is the service layer's job, not the mapper's.
 */
@Component
public class CourseMapper {

    public CourseResponse toResponse(Course course) {
        CourseResponse response = new CourseResponse();
        response.setId(course.getId());
        response.setTitle(course.getTitle());
        response.setDescription(course.getDescription());
        if (course.getTeacher() != null) {
            response.setTeacherId(course.getTeacher().getId());
            response.setTeacherName(course.getTeacher().getName());
        }
        return response;
    }

    public List<CourseResponse> toResponseList(List<Course> courses) {
        return courses.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
