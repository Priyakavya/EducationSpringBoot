package com.education.api.service;

import com.education.api.dto.request.EnrollmentRequest;
import com.education.api.dto.response.EnrollmentResponse;

import java.util.List;

public interface EnrollmentService {

    EnrollmentResponse create(EnrollmentRequest request);

    List<EnrollmentResponse> findAll(Long studentId, Long courseId);

    EnrollmentResponse findById(Long id);

    EnrollmentResponse update(Long id, EnrollmentRequest request);

    void delete(Long id);
}
