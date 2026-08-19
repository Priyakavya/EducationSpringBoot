package com.education.api.controller;

import com.education.api.dto.request.StudentRequest;
import com.education.api.dto.response.EnrollmentResponse;
import com.education.api.dto.response.StudentResponse;
import com.education.api.service.StudentService;
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

import java.net.URI;
import java.util.List;

/**
 * Thin HTTP layer: bind, delegate, choose a status code. No business logic.
 */
@RestController
@RequestMapping("/api/students")
@Tag(name = "Students", description = "Student registration and profile management")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * POST /api/students -> 201 Created
     * Also sets the Location header to the new resource, which is what REST
     * expects after a successful create.
     */
    @PostMapping
    @Operation(summary = "Create a new student")
    public ResponseEntity<StudentResponse> create(@Valid @RequestBody StudentRequest request) {
        StudentResponse created = studentService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    /** GET /api/students?name=raj -> 200 OK */
    @GetMapping
    @Operation(summary = "List all students, optionally filtered by name")
    public ResponseEntity<List<StudentResponse>> findAll(
            @RequestParam(required = false) String name) {
        return ResponseEntity.ok(studentService.findAll(name));
    }

    /** GET /api/students/{id} -> 200 OK or 404 Not Found */
    @GetMapping("/{id}")
    @Operation(summary = "Get a single student by id")
    public ResponseEntity<StudentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.findById(id));
    }

    /** PUT /api/students/{id} -> 200 OK */
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing student")
    public ResponseEntity<StudentResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody StudentRequest request) {
        return ResponseEntity.ok(studentService.update(id, request));
    }

    /** DELETE /api/students/{id} -> 204 No Content (no body, by definition) */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a student and their enrollments")
    public void delete(@PathVariable Long id) {
        studentService.delete(id);
    }

    /** GET /api/students/{id}/enrollments -> the student portal view */
    @GetMapping("/{id}/enrollments")
    @Operation(summary = "List every course this student is enrolled in")
    public ResponseEntity<List<EnrollmentResponse>> findEnrollments(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.findEnrollmentsByStudentId(id));
    }
    @GetMapping("/search")
    public ResponseEntity<Page<StudentResponse>> searchStudentsByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(studentService.searchStudentsByName(name, pageable));
    }
}
