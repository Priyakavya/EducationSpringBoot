package com.education.api.controller;

import com.education.api.dto.request.ExamRequest;
import com.education.api.dto.response.ExamResponse;
import com.education.api.service.ExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/exams")
@Tag(name = "Exams", description = "Exam scheduling per course")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping
    @Operation(summary = "Schedule a new exam for a course")
    public ResponseEntity<ExamResponse> create(@Valid @RequestBody ExamRequest request) {
        ExamResponse created = examService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    /**
     * @DateTimeFormat is what turns ?from=2026-09-01 into a LocalDate.
     * Without it Spring throws a type-mismatch on the query parameter.
     */
    @GetMapping
    @Operation(summary = "List all exams, optionally by course or date window")
    public ResponseEntity<List<ExamResponse>> findAll(
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(examService.findAll(courseId, from, to));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a single exam by id")
    public ResponseEntity<ExamResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(examService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an exam")
    public ResponseEntity<ExamResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody ExamRequest request) {
        return ResponseEntity.ok(examService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an exam and its grades")
    public void delete(@PathVariable Long id) {
        examService.delete(id);
    }
}
