package com.education.api.service;

import com.education.api.dto.request.ExamRequest;
import com.education.api.dto.response.ExamResponse;

import java.time.LocalDate;
import java.util.List;

public interface ExamService {

    ExamResponse create(ExamRequest request);

    List<ExamResponse> findAll(Long courseId, LocalDate from, LocalDate to);

    ExamResponse findById(Long id);

    ExamResponse update(Long id, ExamRequest request);

    void delete(Long id);
}
