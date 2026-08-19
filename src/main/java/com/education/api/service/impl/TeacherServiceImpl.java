package com.education.api.service.impl;

import com.education.api.dto.request.TeacherRequest;
import com.education.api.dto.response.CourseResponse;
import com.education.api.dto.response.TeacherResponse;
import com.education.api.entity.Teacher;
import com.education.api.exception.BusinessRuleException;
import com.education.api.exception.DuplicateResourceException;
import com.education.api.exception.ResourceNotFoundException;
import com.education.api.mapper.CourseMapper;
import com.education.api.mapper.TeacherMapper;
import com.education.api.repository.CourseRepository;
import com.education.api.repository.TeacherRepository;
import com.education.api.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TeacherServiceImpl implements TeacherService {

    private static final Logger log = LoggerFactory.getLogger(TeacherServiceImpl.class);

    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final TeacherMapper teacherMapper;
    private final CourseMapper courseMapper;

    public TeacherServiceImpl(TeacherRepository teacherRepository,
                              CourseRepository courseRepository,
                              TeacherMapper teacherMapper,
                              CourseMapper courseMapper) {
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
        this.teacherMapper = teacherMapper;
        this.courseMapper = courseMapper;
    }

    // ------------------------------------------------------------------ CREATE
    @Override
    @Transactional
    public TeacherResponse create(TeacherRequest request) {
        if (teacherRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new DuplicateResourceException("Teacher", "email", request.getEmail());
        }
        Teacher saved = teacherRepository.save(teacherMapper.toEntity(request));
        log.info("Created teacher id={}", saved.getId());
        return teacherMapper.toResponse(saved);
    }

    // ------------------------------------------------------------------ READ
    @Override
    public List<TeacherResponse> findAll(String departmentFilter) {
        List<Teacher> teachers = StringUtils.hasText(departmentFilter)
                ? teacherRepository.findByDepartmentIgnoreCase(departmentFilter)
                : teacherRepository.findAll();
        return teacherMapper.toResponseList(teachers);
    }

    @Override
    public TeacherResponse findById(Long id) {
        return teacherMapper.toResponse(getTeacherOrThrow(id));
    }

    // ------------------------------------------------------------------ UPDATE
    @Override
    @Transactional
    public TeacherResponse update(Long id, TeacherRequest request) {
        Teacher teacher = getTeacherOrThrow(id);
        if (teacherRepository.existsByEmailIgnoreCaseAndIdNot(request.getEmail(), id)) {
            throw new DuplicateResourceException("Teacher", "email", request.getEmail());
        }
        teacherMapper.updateEntity(teacher, request);
        Teacher updated = teacherRepository.save(teacher);
        log.info("Updated teacher id={}", id);
        return teacherMapper.toResponse(updated);
    }

    // ------------------------------------------------------------------ DELETE
    @Override
    @Transactional
    public void delete(Long id) {
        Teacher teacher = getTeacherOrThrow(id);

        // RULE: a teacher who still owns courses cannot be deleted. Cascading
        // here would silently destroy courses, enrolments and grades.
        if (courseRepository.existsByTeacherId(id)) {
            throw new BusinessRuleException(
                    "Teacher id " + id + " still owns courses. Reassign or delete those courses first.");
        }

        teacherRepository.delete(teacher);
        log.info("Deleted teacher id={}", id);
    }

    // ------------------------------------------------------------------ EXTRA
    @Override
    public List<CourseResponse> findCoursesByTeacherId(Long teacherId) {
        getTeacherOrThrow(teacherId);
        return courseMapper.toResponseList(courseRepository.findByTeacherId(teacherId));
    }

    // ------------------------------------------------------------------ helper
    private Teacher getTeacherOrThrow(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", id));
    }
}
