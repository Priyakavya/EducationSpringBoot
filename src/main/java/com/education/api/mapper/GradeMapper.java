package com.education.api.mapper;

import com.education.api.dto.request.GradeRequest;
import com.education.api.dto.response.GradeResponse;
import com.education.api.entity.Enrollment;
import com.education.api.entity.Exam;
import com.education.api.entity.Grade;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GradeMapper {

    public GradeResponse toResponse(Grade grade) {
        GradeResponse response = new GradeResponse();
        response.setId(grade.getId());
        response.setScore(grade.getScore());
        response.setLetterGrade(grade.getLetterGrade());
        response.setStatus(grade.getStatus());

        Enrollment enrollment = grade.getEnrollment();
        if (enrollment != null) {
            response.setEnrollmentId(enrollment.getId());
            if (enrollment.getStudent() != null) {
                response.setStudentId(enrollment.getStudent().getId());
                response.setStudentName(enrollment.getStudent().getName());
            }
            if (enrollment.getCourse() != null) {
                response.setCourseTitle(enrollment.getCourse().getTitle());
            }
        }
        if (grade.getExam() != null) {
            response.setExamId(grade.getExam().getId());
            response.setExamTitle(grade.getExam().getTitle());
        }
        return response;
    }

    public List<GradeResponse> toResponseList(List<Grade> grades) {
        return grades.stream().map(this::toResponse).collect(Collectors.toList());
    }
    public Grade toEntity(GradeRequest request, Enrollment enrollment, Exam exam) {
        Grade grade = new Grade();
        grade.setEnrollment(enrollment);
        grade.setExam(exam);
        grade.setScore(request.getScore());
        grade.setStatus(request.getStatus());
        return grade;
    }
}
