package com.education.api.controller;

import com.education.api.dto.request.TeacherRequest;
import com.education.api.dto.response.CourseResponse;
import com.education.api.dto.response.TeacherResponse;
import com.education.api.service.TeacherService;
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
@RequestMapping("/api/teachers")
@Tag(name = "Teachers", description = "Teacher records and their course load")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping
    @Operation(summary = "Create a new teacher")
    public ResponseEntity<TeacherResponse> create(@Valid @RequestBody TeacherRequest request) {
        TeacherResponse created = teacherService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    @Operation(summary = "List all teachers, optionally filtered by department")
    public ResponseEntity<List<TeacherResponse>> findAll(
            @RequestParam(required = false) String department) {
        return ResponseEntity.ok(teacherService.findAll(department));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a single teacher by id")
    public ResponseEntity<TeacherResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing teacher")
    public ResponseEntity<TeacherResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody TeacherRequest request) {
        return ResponseEntity.ok(teacherService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a teacher (blocked while they still own courses)")
    public void delete(@PathVariable Long id) {
        teacherService.delete(id);
    }

    /** GET /api/teachers/{id}/courses -> the teacher dashboard view */
    @GetMapping("/{id}/courses")
    @Operation(summary = "List every course owned by this teacher")
    public ResponseEntity<List<CourseResponse>> findCourses(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.findCoursesByTeacherId(id));
    }
}
