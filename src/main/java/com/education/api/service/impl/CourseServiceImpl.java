package com.education.api.service.impl;

import com.education.api.dto.request.CourseRequest;
import com.education.api.dto.response.CourseResponse;
import com.education.api.dto.response.StudentResponse;
import com.education.api.entity.Course;
import com.education.api.entity.Teacher;
import com.education.api.exception.ResourceNotFoundException;
import com.education.api.mapper.CourseMapper;
import com.education.api.mapper.StudentMapper;
import com.education.api.repository.CourseRepository;
import com.education.api.repository.StudentRepository;
import com.education.api.repository.TeacherRepository;
import com.education.api.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import com.education.api.entity.Course;
import com.education.api.entity.Teacher;


import java.util.List;

@Service
@Transactional(readOnly = true)
public class CourseServiceImpl implements CourseService {

    private static final Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final CourseMapper courseMapper;
    private final StudentMapper studentMapper;

    public CourseServiceImpl(CourseRepository courseRepository,
                             TeacherRepository teacherRepository,
                             StudentRepository studentRepository,
                             CourseMapper courseMapper,
                             StudentMapper studentMapper) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.courseMapper = courseMapper;
        this.studentMapper = studentMapper;
    }

    // ------------------------------------------------------------------ CREATE
    @Override
    @Transactional
    public CourseResponse create(CourseRequest request) {
        // RULE: the referenced teacher must exist -> 404, not a raw FK error.
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", request.getTeacherId()));

        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setTeacher(teacher);

        Course saved = courseRepository.save(course);
        log.info("Created course id={} for teacher id={}", saved.getId(), teacher.getId());
        return courseMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void testTransaction() {
        log.info("TX TEST: Starting transaction");

        // STEP 1: Get any existing teacher from DB
        Teacher teacher = teacherRepository.findById(1L) // use id=1, change if needed
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", 1L));

        Course c = new Course();
        c.setTitle("TX Test Course " + System.currentTimeMillis());
        c.setDescription("This should be rolled back");
        c.setTeacher(teacher); // <-- ADD THIS LINE. This fixes teacher_id null error
        
        courseRepository.save(c);

        log.info("TX TEST: Course saved with id = " + c.getId());
        log.info("TX TEST: Now forcing error to rollback");

        int x = 10 / 0; // Force ArithmeticException -> 500
        
        log.info("TX TEST: This line will never run");
    }    
    
    // ------------------------------------------------------------------ READ
    @Override
    public Page<CourseResponse> findAll(String title, Pageable pageable) {
        Page<Course> courses;
        
        if(!StringUtils.hasText(title)) {
            courses = courseRepository.findAll(pageable);
        } else {
            courses = courseRepository.findByTitleContainingIgnoreCase(title, pageable);
        }
        
        return courses.map(courseMapper::toResponse);
    }

    @Override
    public CourseResponse findById(Long id) {
        Course course = courseRepository.findByIdWithTeacher(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", id));
        return courseMapper.toResponse(course);
    }

    // ------------------------------------------------------------------ UPDATE
    @Override
    @Transactional
    public CourseResponse update(Long id, CourseRequest request) {
        Course course = getCourseOrThrow(id);

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());

        // Reassigning the course to a different teacher is allowed.
        if (!course.getTeacher().getId().equals(request.getTeacherId())) {
            Teacher newTeacher = teacherRepository.findById(request.getTeacherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher", request.getTeacherId()));
            course.setTeacher(newTeacher);
            log.info("Course id={} reassigned to teacher id={}", id, newTeacher.getId());
        }

        Course updated = courseRepository.save(course);
        return courseMapper.toResponse(updated);
    }

    // ------------------------------------------------------------------ DELETE
    @Override
    @Transactional
    public void delete(Long id) {
        Course course = getCourseOrThrow(id);
        // Cascade removes the course's enrolments and exams, and through those,
        // the grades. Deleting a course is an admin action, so this is intended.
        courseRepository.delete(course);
        log.info("Deleted course id={}", id);
    }

    // ------------------------------------------------------------------ EXTRA
    @Override
    public List<StudentResponse> findStudentsByCourseId(Long courseId) {
        getCourseOrThrow(courseId);
        return studentMapper.toResponseList(studentRepository.findAllByCourseId(courseId));
    }

    // ------------------------------------------------------------------ helper
    private Course getCourseOrThrow(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", id));
    }
    @Override
    public Page<CourseResponse> searchCoursesByName(String title, Pageable pageable) {
    	return courseRepository.findByTitleContainingIgnoreCase(title, pageable)
    		    .map(courseMapper::toResponse);
}
}
