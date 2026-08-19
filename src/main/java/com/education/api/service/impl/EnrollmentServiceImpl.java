package com.education.api.service.impl;

import com.education.api.dto.request.EnrollmentRequest;
import com.education.api.dto.response.EnrollmentResponse;
import com.education.api.entity.Course;
import com.education.api.entity.Enrollment;
import com.education.api.entity.Student;
import com.education.api.exception.DuplicateResourceException;
import com.education.api.exception.ResourceNotFoundException;
import com.education.api.mapper.EnrollmentMapper;
import com.education.api.repository.CourseRepository;
import com.education.api.repository.EnrollmentRepository;
import com.education.api.repository.StudentRepository;
import com.education.api.service.EnrollmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EnrollmentServiceImpl implements EnrollmentService {

    private static final Logger log = LoggerFactory.getLogger(EnrollmentServiceImpl.class);

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository,
                                 StudentRepository studentRepository,
                                 CourseRepository courseRepository,
                                 EnrollmentMapper enrollmentMapper) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentMapper = enrollmentMapper;
    }

    // ------------------------------------------------------------------ CREATE
    @Override
    @Transactional
    public EnrollmentResponse create(EnrollmentRequest request) {
        // RULE 1: both sides of the link must exist.
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", request.getStudentId()));
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", request.getCourseId()));

        // RULE 2: no double enrolment. Also guarded by a UNIQUE key in the DB,
        // so two simultaneous requests still cannot slip through.
        if (enrollmentRepository.existsByStudentIdAndCourseId(request.getStudentId(), request.getCourseId())) {
            throw new DuplicateResourceException(
                    "Student id " + request.getStudentId()
                            + " is already enrolled in course id " + request.getCourseId());
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(request.getEnrollmentDate());

        Enrollment saved = enrollmentRepository.save(enrollment);
        log.info("Enrolled student id={} in course id={} (enrollment id={})",
                student.getId(), course.getId(), saved.getId());
        return enrollmentMapper.toResponse(saved);
    }

    // ------------------------------------------------------------------ READ
    @Override
    public List<EnrollmentResponse> findAll(Long studentId, Long courseId) {
        List<Enrollment> enrollments;
        if (studentId != null) {
            enrollments = enrollmentRepository.findByStudentId(studentId);
        } else if (courseId != null) {
            enrollments = enrollmentRepository.findByCourseId(courseId);
        } else {
            enrollments = enrollmentRepository.findAllWithDetails();
        }
        return enrollmentMapper.toResponseList(enrollments);
    }

    @Override
    public EnrollmentResponse findById(Long id) {
        Enrollment enrollment = enrollmentRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", id));
        return enrollmentMapper.toResponse(enrollment);
    }

    // ------------------------------------------------------------------ UPDATE
    @Override
    @Transactional
    public EnrollmentResponse update(Long id, EnrollmentRequest request) {
        Enrollment enrollment = getEnrollmentOrThrow(id);

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", request.getStudentId()));
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", request.getCourseId()));

        // Same duplicate rule, excluding the row being edited.
        if (enrollmentRepository.existsByStudentIdAndCourseIdAndIdNot(
                request.getStudentId(), request.getCourseId(), id)) {
            throw new DuplicateResourceException(
                    "Student id " + request.getStudentId()
                            + " is already enrolled in course id " + request.getCourseId());
        }

        enrollment.setStudent(student);
        enrollment.setCourse(course);
        if (request.getEnrollmentDate() != null) {
            enrollment.setEnrollmentDate(request.getEnrollmentDate());
        }

        Enrollment updated = enrollmentRepository.save(enrollment);
        log.info("Updated enrollment id={}", id);
        return enrollmentMapper.toResponse(updated);
    }

    // ------------------------------------------------------------------ DELETE
    @Override
    @Transactional
    public void delete(Long id) {
        Enrollment enrollment = getEnrollmentOrThrow(id);
        // Cascade removes this enrolment's grades - a grade without an
        // enrolment would be an orphan row with no student attached.
        enrollmentRepository.delete(enrollment);
        log.info("Deleted enrollment id={}", id);
    }

    // ------------------------------------------------------------------ helper
    private Enrollment getEnrollmentOrThrow(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", id));
    }
}
