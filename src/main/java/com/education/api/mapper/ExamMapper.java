package com.education.api.mapper;

import com.education.api.dto.response.ExamResponse;
import com.education.api.entity.Exam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExamMapper {

    public ExamResponse toResponse(Exam exam) {
        ExamResponse response = new ExamResponse();
        response.setId(exam.getId());
        response.setTitle(exam.getTitle());
        response.setExamDate(exam.getExamDate());
        if (exam.getCourse() != null) {
            response.setCourseId(exam.getCourse().getId());
            response.setCourseTitle(exam.getCourse().getTitle());
        }
        return response;
    }

    public List<ExamResponse> toResponseList(List<Exam> exams) {
        return exams.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
