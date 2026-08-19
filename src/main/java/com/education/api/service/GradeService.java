package com.education.api.service;

import com.education.api.dto.request.GradeRequest;
import com.education.api.dto.response.GradeResponse;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface GradeService {

    GradeResponse create(GradeRequest request);

    List<GradeResponse> findAll(Long studentId, Long examId, Long enrollmentId);

    GradeResponse findById(Long id);

    GradeResponse update(Long id, GradeRequest request);

    void delete(Long id);

    /** Extra: GET /api/grades/exam/{examId}/average */
    Double findAverageScoreByExamId(Long examId);
    void processUpload(MultipartFile file) throws IOException;

}
