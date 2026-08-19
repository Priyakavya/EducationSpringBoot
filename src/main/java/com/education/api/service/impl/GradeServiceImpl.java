package com.education.api.service.impl;

import java.util.List;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.scheduling.annotation.Scheduled;
import com.education.api.dto.request.GradeRequest;
import com.education.api.dto.response.GradeResponse;
import com.education.api.entity.Enrollment;
import com.education.api.entity.Exam;
import com.education.api.entity.Grade;
import com.education.api.exception.BusinessRuleException;
import com.education.api.exception.DuplicateResourceException;
import com.education.api.exception.ResourceNotFoundException;
import com.education.api.mapper.GradeMapper;
import com.education.api.repository.EnrollmentRepository;
import com.education.api.repository.ExamRepository;
import com.education.api.repository.GradeRepository;
import com.education.api.service.GradeService;
@Service
@Transactional(readOnly = true)
public class GradeServiceImpl implements GradeService {

    private static final Logger log = LoggerFactory.getLogger(GradeServiceImpl.class);

    private final GradeRepository gradeRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ExamRepository examRepository;
    private final GradeMapper gradeMapper;

    public GradeServiceImpl(GradeRepository gradeRepository,
                            EnrollmentRepository enrollmentRepository,
                            ExamRepository examRepository,
                            GradeMapper gradeMapper) {
        this.gradeRepository = gradeRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.examRepository = examRepository;
        this.gradeMapper = gradeMapper;
    }

    // ------------------------------------------------------------------ CREATE
    @Override
    @Transactional
    public GradeResponse create(GradeRequest request) {
        Enrollment enrollment = enrollmentRepository.findById(request.getEnrollmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", request.getEnrollmentId()));
        Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(() -> new ResourceNotFoundException("Exam", request.getExamId()));

        // RULE 1: the exam must belong to the course the student is enrolled in.
        // This is the rule that stops a Maths mark landing on a History enrolment.
        validateExamBelongsToEnrolledCourse(enrollment, exam);

        // RULE 2: one score per enrolment per exam.
        if (gradeRepository.existsByEnrollmentIdAndExamId(request.getEnrollmentId(), request.getExamId())) {
            throw new DuplicateResourceException(
                    "A grade already exists for enrollment id " + request.getEnrollmentId()
                            + " and exam id " + request.getExamId() + ". Use PUT to change it.");
        }

        Grade grade = new Grade();
        grade.setEnrollment(enrollment);
        grade.setExam(exam);
        grade.setScore(request.getScore());

        Grade saved = gradeRepository.save(grade);
        log.info("Recorded grade id={} score={} for enrollment id={} exam id={}",
                saved.getId(), saved.getScore(), enrollment.getId(), exam.getId());
        return gradeMapper.toResponse(saved);
    }

    // ------------------------------------------------------------------ READ
    @Override
    public List<GradeResponse> findAll(Long studentId, Long examId, Long enrollmentId) {
        List<Grade> grades;
        if (studentId != null) {
            grades = gradeRepository.findAllByStudentId(studentId);
        } else if (examId != null) {
            grades = gradeRepository.findByExamId(examId);
        } else if (enrollmentId != null) {
            grades = gradeRepository.findByEnrollmentId(enrollmentId);
        } else {
            grades = gradeRepository.findAllWithDetails();
        }
        return gradeMapper.toResponseList(grades);
    }

    @Override
    public GradeResponse findById(Long id) {
        Grade grade = gradeRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade", id));
        return gradeMapper.toResponse(grade);
    }

    // ------------------------------------------------------------------ UPDATE
    @Override
    @Transactional
    public GradeResponse update(Long id, GradeRequest request) {
        Grade grade = getGradeOrThrow(id);

        Enrollment enrollment = enrollmentRepository.findById(request.getEnrollmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", request.getEnrollmentId()));
        Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(() -> new ResourceNotFoundException("Exam", request.getExamId()));

        validateExamBelongsToEnrolledCourse(enrollment, exam);

        if (gradeRepository.existsByEnrollmentIdAndExamIdAndIdNot(
                request.getEnrollmentId(), request.getExamId(), id)) {
            throw new DuplicateResourceException(
                    "Another grade already exists for enrollment id " + request.getEnrollmentId()
                            + " and exam id " + request.getExamId());
        }

        grade.setEnrollment(enrollment);
        grade.setExam(exam);
        grade.setScore(request.getScore());

        Grade updated = gradeRepository.save(grade);
        log.info("Updated grade id={} to score={}", id, updated.getScore());
        return gradeMapper.toResponse(updated);
    }

    // ------------------------------------------------------------------ DELETE
    @Override
    @Transactional
    public void delete(Long id) {
        Grade grade = getGradeOrThrow(id);
        gradeRepository.delete(grade);
        log.info("Deleted grade id={}", id);
    }

    // ------------------------------------------------------------------ EXTRA
    @Override
    public Double findAverageScoreByExamId(Long examId) {
        if (!examRepository.existsById(examId)) {
            throw new ResourceNotFoundException("Exam", examId);
        }
        Double average = gradeRepository.findAverageScoreByExamId(examId);
        // AVG() over zero rows returns null - report 0.0 rather than a null body.
        return average != null ? average : 0.0;
    }

    // ------------------------------------------------------------------ helpers
    private void validateExamBelongsToEnrolledCourse(Enrollment enrollment, Exam exam) {
        Long enrolledCourseId = enrollment.getCourse().getId();
        Long examCourseId = exam.getCourse().getId();
        if (!enrolledCourseId.equals(examCourseId)) {
            throw new BusinessRuleException(
                    "Exam id " + exam.getId() + " belongs to course id " + examCourseId
                            + ", but enrollment id " + enrollment.getId()
                            + " is for course id " + enrolledCourseId + ".");
        }
    }

    private Grade getGradeOrThrow(Long id) {
        return gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade", id));
    }
    @Override
    @Transactional
    public void processUpload(MultipartFile file) throws IOException {
        log.info("Processing uploaded file: {}", file.getOriginalFilename());

        try(BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            br.readLine(); // skip header row
            while ((line = br.readLine())!= null) {
                String[] data = line.split(",");
                log.info("CSV Row: {}", Arrays.toString(data));
                // data[0]=studentId, data[1]=examId, data[2]=score
            }
        } // closes try
    } // closes method
}
        
    
    

