package com.education.api.service.impl;

import com.education.api.dto.request.StudentRequest;
import com.education.api.dto.response.EnrollmentResponse;
import com.education.api.dto.response.StudentResponse;
import com.education.api.entity.Student;
import com.education.api.exception.DuplicateResourceException;
import com.education.api.exception.ResourceNotFoundException;
import com.education.api.mapper.EnrollmentMapper;
import com.education.api.mapper.StudentMapper;
import com.education.api.repository.EnrollmentRepository;
import com.education.api.repository.StudentRepository;
import com.education.api.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.data.domain.Page;     
import org.springframework.data.domain.Pageable; 
import org.springframework.data.domain.PageRequest; 
import org.springframework.data.domain.Sort;     
import java.util.List;

/**
 * All business rules for students live here. Controllers stay thin; repositories
 * stay dumb.
 *
 * readOnly = true on the class puts every method in a read-only transaction
 * (Hibernate skips dirty checking - measurably faster); the writing methods
 * override it with their own @Transactional.
 */
@Service
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentMapper studentMapper;
    private final EnrollmentMapper enrollmentMapper;

    /** Constructor injection - no @Autowired needed on a single constructor. */
    public StudentServiceImpl(StudentRepository studentRepository,
                              EnrollmentRepository enrollmentRepository,
                              StudentMapper studentMapper,
                              EnrollmentMapper enrollmentMapper) {
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.studentMapper = studentMapper;
        this.enrollmentMapper = enrollmentMapper;
    }

    // ------------------------------------------------------------------ CREATE
    @Override
    @Transactional
    public StudentResponse create(StudentRequest request) {
        log.info("Creating student with email {}", request.getEmail());

        // RULE 1: email must be unique across all students.
        if (studentRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new DuplicateResourceException("Student", "email", request.getEmail());
        }

        Student student = studentMapper.toEntity(request);
        Student saved = studentRepository.save(student);
        log.info("Created student id={}", saved.getId());
        return studentMapper.toResponse(saved);
    }

    // ------------------------------------------------------------------ READ
    @Override
    public List<StudentResponse> findAll(String nameFilter) {
        List<Student> students = StringUtils.hasText(nameFilter)
                ? studentRepository.findByNameContainingIgnoreCase(nameFilter)
                : studentRepository.findAll();
        return studentMapper.toResponseList(students);
    }

    @Override
    public StudentResponse findById(Long id) {
        Student student = getStudentOrThrow(id);
        return studentMapper.toResponse(student);
    }

    // ------------------------------------------------------------------ UPDATE
    @Override
    @Transactional
    public StudentResponse update(Long id, StudentRequest request) {
        Student student = getStudentOrThrow(id);

        // RULE 2: the new email must not belong to a DIFFERENT student.
        // Without the "AndIdNot" part, saving a student without changing their
        // email would falsely trip the duplicate check.
        if (studentRepository.existsByEmailIgnoreCaseAndIdNot(request.getEmail(), id)) {
            throw new DuplicateResourceException("Student", "email", request.getEmail());
        }

        studentMapper.updateEntity(student, request);
        // No explicit save() needed inside a transaction - Hibernate's dirty
        // checking flushes the change. save() is kept for readability.
        Student updated = studentRepository.save(student);
        log.info("Updated student id={}", id);
        return studentMapper.toResponse(updated);
    }

    // ------------------------------------------------------------------ DELETE
    @Override
    @Transactional
    public void delete(Long id) {
        Student student = getStudentOrThrow(id);
        // Cascade on Student.enrollments removes the enrolments (and, through
        // Enrollment.grades, that student's grades) in one go.
        studentRepository.delete(student);
        log.info("Deleted student id={}", id);
    }

    // ------------------------------------------------------------------ EXTRA
    @Override
    public List<EnrollmentResponse> findEnrollmentsByStudentId(Long studentId) {
        // Verify the student exists so the caller gets 404 rather than [].
        getStudentOrThrow(studentId);
        return enrollmentMapper.toResponseList(enrollmentRepository.findByStudentId(studentId));
    }

    // ------------------------------------------------------------------ helper
    private Student getStudentOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));
    }
    @Override
    public Page<StudentResponse> searchStudentsByName(String name, Pageable pageable) {
        return studentRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(studentMapper::toResponse);
    }

    
    }

