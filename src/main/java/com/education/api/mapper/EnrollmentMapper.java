package com.education.api.mapper;

import com.education.api.dto.response.EnrollmentResponse;
import com.education.api.entity.Enrollment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EnrollmentMapper {

    public EnrollmentResponse toResponse(Enrollment enrollment) {
        EnrollmentResponse response = new EnrollmentResponse();
        response.setId(enrollment.getId());
        response.setEnrollmentDate(enrollment.getEnrollmentDate());
        if (enrollment.getStudent() != null) {
            response.setStudentId(enrollment.getStudent().getId());
            response.setStudentName(enrollment.getStudent().getName());
        }
        if (enrollment.getCourse() != null) {
            response.setCourseId(enrollment.getCourse().getId());
            response.setCourseTitle(enrollment.getCourse().getTitle());
        }
        return response;
    }

    public List<EnrollmentResponse> toResponseList(List<Enrollment> enrollments) {
        return enrollments.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
