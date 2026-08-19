package com.education.api.mapper;

import com.education.api.dto.request.StudentRequest;
import com.education.api.dto.response.StudentResponse;
import com.education.api.entity.Student;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Entity <-> DTO translation. Kept out of the service so the service only
 * deals in business rules.
 */
@Component
public class StudentMapper {

    /** Request -> brand new entity (used by POST). */
    public Student toEntity(StudentRequest request) {
        Student student = new Student();
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setEnrollmentDate(request.getEnrollmentDate());
        return student;
    }

    /**
     * Copies request fields onto an entity already managed by the persistence
     * context (used by PUT). The id is never touched.
     */
    public void updateEntity(Student student, StudentRequest request) {
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        if (request.getEnrollmentDate() != null) {
            student.setEnrollmentDate(request.getEnrollmentDate());
        }
    }

    public StudentResponse toResponse(Student student) {
        StudentResponse response = new StudentResponse();
        response.setId(student.getId());
        response.setName(student.getName());
        response.setEmail(student.getEmail());
        response.setEnrollmentDate(student.getEnrollmentDate());
        response.setTotalEnrollments(student.getEnrollments() != null ? student.getEnrollments().size() : 0);
        return response;
    }

    public List<StudentResponse> toResponseList(List<Student> students) {
        return students.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
