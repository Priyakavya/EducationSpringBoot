package com.education.api.service.impl;

import com.education.api.dto.request.ExamRequest;
import com.education.api.dto.response.ExamResponse;
import com.education.api.entity.Course;
import com.education.api.entity.Exam;
import com.education.api.exception.BusinessRuleException;
import com.education.api.exception.ResourceNotFoundException;
import com.education.api.mapper.ExamMapper;
import com.education.api.repository.CourseRepository;
import com.education.api.repository.ExamRepository;
import com.education.api.service.ExamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ExamServiceImpl implements ExamService {

    private static final Logger log = LoggerFactory.getLogger(ExamServiceImpl.class);

    private final ExamRepository examRepository;
    private final CourseRepository courseRepository;
    private final ExamMapper examMapper;

    public ExamServiceImpl(ExamRepository examRepository,
                           CourseRepository courseRepository,
                           ExamMapper examMapper) {
        this.examRepository = examRepository;
        this.courseRepository = courseRepository;
        this.examMapper = examMapper;
    }

    // ------------------------------------------------------------------ CREATE
    @Override
    @Transactional
    public ExamResponse create(ExamRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", request.getCourseId()));

        Exam exam = new Exam();
        exam.setCourse(course);
        exam.setExamDate(request.getExamDate());
        exam.setTitle(request.getTitle());

        Exam saved = examRepository.save(exam);
        log.info("Created exam id={} for course id={}", saved.getId(), course.getId());
        return examMapper.toResponse(saved);
    }

    // ------------------------------------------------------------------ READ
    @Override
    public List<ExamResponse> findAll(Long courseId, LocalDate from, LocalDate to) {
        List<Exam> exams;
        if (courseId != null) {
            exams = examRepository.findByCourseId(courseId);
        } else if (from != null && to != null) {
            // RULE: a reversed window is a client mistake, not an empty result.
            if (from.isAfter(to)) {
                throw new BusinessRuleException("'from' date must not be after 'to' date");
            }
            exams = examRepository.findByExamDateBetween(from, to);
        } else {
            exams = examRepository.findAllWithCourse();
        }
        return examMapper.toResponseList(exams);
    }

    @Override
    public ExamResponse findById(Long id) {
        Exam exam = examRepository.findByIdWithCourse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam", id));
        return examMapper.toResponse(exam);
    }

    // ------------------------------------------------------------------ UPDATE
    @Override
    @Transactional
    public ExamResponse update(Long id, ExamRequest request) {
        Exam exam = getExamOrThrow(id);

        if (!exam.getCourse().getId().equals(request.getCourseId())) {
            // RULE: moving an exam to another course would strand the grades
            // already recorded against enrolments of the original course.
            if (!exam.getGrades().isEmpty()) {
                throw new BusinessRuleException(
                        "Exam id " + id + " already has grades recorded and cannot be moved to another course.");
            }
            Course newCourse = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course", request.getCourseId()));
            exam.setCourse(newCourse);
        }

        exam.setExamDate(request.getExamDate());
        exam.setTitle(request.getTitle());

        Exam updated = examRepository.save(exam);
        log.info("Updated exam id={}", id);
        return examMapper.toResponse(updated);
    }

    // ------------------------------------------------------------------ DELETE
    @Override
    @Transactional
    public void delete(Long id) {
        Exam exam = getExamOrThrow(id);
        examRepository.delete(exam);
        log.info("Deleted exam id={}", id);
    }

    // ------------------------------------------------------------------ helper
    private Exam getExamOrThrow(Long id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam", id));
    }
}
