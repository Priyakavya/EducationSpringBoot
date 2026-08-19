package com.education.api.controller;

import com.education.api.dto.request.EnrollmentRequest;
import com.education.api.dto.response.EnrollmentResponse;
import com.education.api.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@Tag(name = "Enrollments", description = "Links students to courses (many-to-many)")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    @Operation(summary = "Enroll a student in a course")
    public ResponseEntity<EnrollmentResponse> create(@Valid @RequestBody EnrollmentRequest request) {
        EnrollmentResponse created = enrollmentService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    @Operation(summary = "List all enrollments, optionally filtered by student or course")
    public ResponseEntity<List<EnrollmentResponse>> findAll(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long courseId) {
        return ResponseEntity.ok(enrollmentService.findAll(studentId, courseId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a single enrollment by id")
    public ResponseEntity<EnrollmentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(enrollmentService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an enrollment")
    public ResponseEntity<EnrollmentResponse> update(@PathVariable Long id,
                                                     @Valid @RequestBody EnrollmentRequest request) {
        return ResponseEntity.ok(enrollmentService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove an enrollment and its grades")
    public void delete(@PathVariable Long id) {
        enrollmentService.delete(id);
    }
}
