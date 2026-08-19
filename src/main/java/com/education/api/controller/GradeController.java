package com.education.api.controller;
import com.education.api.dto.request.GradeRequest;
import com.education.api.dto.response.GradeResponse;
import com.education.api.service.GradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/grades")
@Tag(name = "Grades", description = "Scores recorded per student per exam")
public class GradeController {

    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @PostMapping
    @Operation(summary = "Record a grade for an enrollment on an exam")
    public ResponseEntity<GradeResponse> create(@Valid @RequestBody GradeRequest request) {
        GradeResponse created = gradeService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }
    @PostMapping("/upload")
    @Operation(summary = "Upload grades from CSV file")
    public ResponseEntity<String> uploadGrades(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file");
        }
        
        // Call service to process file
        gradeService.processUpload(file);
        
        return ResponseEntity.ok("File uploaded successfully: " + file.getOriginalFilename());
    }
    @GetMapping
    @Operation(summary = "List all grades, optionally by student, exam or enrollment")
    public ResponseEntity<List<GradeResponse>> findAll(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long examId,
            @RequestParam(required = false) Long enrollmentId) {
        return ResponseEntity.ok(gradeService.findAll(studentId, examId, enrollmentId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a single grade by id")
    public ResponseEntity<GradeResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(gradeService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Change a recorded grade")
    public ResponseEntity<GradeResponse> update(@PathVariable Long id,
                                                @Valid @RequestBody GradeRequest request) {
        return ResponseEntity.ok(gradeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a grade")
    public void delete(@PathVariable Long id) {
        gradeService.delete(id);
    }

    /** GET /api/grades/exam/{examId}/average -> class average for one exam. */
    @GetMapping("/exam/{examId}/average")
    @Operation(summary = "Average score across all students for one exam")
    public ResponseEntity<Map<String, Object>> averageForExam(@PathVariable Long examId) {
        Double average = gradeService.findAverageScoreByExamId(examId);
        Map<String, Object> body = new HashMap<>();
        body.put("examId", examId);
        body.put("averageScore", Math.round(average * 100.0) / 100.0);
        return ResponseEntity.ok(body);
        
        
        }
    
    }

