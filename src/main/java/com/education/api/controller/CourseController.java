package com.education.api.controller;

import com.education.api.dto.request.CourseRequest;
import com.education.api.dto.response.CourseResponse;
import com.education.api.dto.response.StudentResponse;
import com.education.api.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URI;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import java.io.IOException;


@RestController
@RequestMapping("/api/courses")
@Tag(name = "Courses", description = "Course catalogue and teacher assignment")
public class CourseController {
	private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }
    
    @PostMapping
    @Operation(summary = "Create a new course and assign it to a teacher")
    public ResponseEntity<CourseResponse> create(@Valid @RequestBody CourseRequest request) {
        CourseResponse created = courseService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }
    @PostMapping("/test-tx") // <-- NEW METHOD
    @Operation(summary = "Test Transaction Rollback")
    public ResponseEntity<String> testTransaction() {
        logger.info("Calling testTransaction API");
        courseService.testTransaction(); // this will throw error
        return ResponseEntity.ok("Transaction completed");
    }


        @GetMapping("/{id}")
    @Operation(summary = "Get a single course by id")
    public ResponseEntity<CourseResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a course or reassign it to another teacher")
    public ResponseEntity<CourseResponse> update(@PathVariable Long id,
                                                 @Valid @RequestBody CourseRequest request) {
        return ResponseEntity.ok(courseService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a course with its enrollments and exams")
    public void delete(@PathVariable Long id) {
        courseService.delete(id);
    }

    /** GET /api/courses/{id}/students -> the class roster */
    @GetMapping("/{id}/students")
    @Operation(summary = "List every student enrolled in this course")
    public ResponseEntity<List<StudentResponse>> findStudents(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.findStudentsByCourseId(id));
    }
    @GetMapping
    @Operation(summary = "List all courses with pagination, sorting and optional filter")
    public ResponseEntity<Page<CourseResponse>> findAll(
        @RequestParam(required = false) String title,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "title") String sortBy,
        @RequestParam(defaultValue = "asc") String sortDir
    ) {
    	logger.info("Fetching courses -> title: {}, page: {}, size: {}, sortBy: {}, sortDir: {}", 
                title, page, size, sortBy, sortDir);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        return ResponseEntity.ok(courseService.findAll(title, pageable));
    }
    @PostMapping("/{id}/upload")
    public ResponseEntity<String> uploadFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws IOException {
        
        String uploadDir = "uploads/";
        Files.createDirectories(Paths.get(uploadDir));

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);
        Files.write(filePath, file.getBytes());

        return ResponseEntity.ok("File uploaded successfully: " + fileName);
    }
}