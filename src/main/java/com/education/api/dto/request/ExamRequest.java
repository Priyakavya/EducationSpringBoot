package com.education.api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class ExamRequest {

    @NotNull(message = "courseId is required")
    @Positive(message = "courseId must be a positive number")
    private Long courseId;

    /** Exams are scheduled, so a future date is perfectly valid here. */
    @NotNull(message = "examDate is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate examDate;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 150, message = "Title must be between 3 and 150 characters")
    private String title;

    // ------------------------------------------------------------------
    //  Constructors
    // ------------------------------------------------------------------

    public ExamRequest() {
    }

    public ExamRequest(Long courseId, LocalDate examDate, String title) {
        this.courseId = courseId;
        this.examDate = examDate;
        this.title = title;
    }

    // ------------------------------------------------------------------
    //  Getters and Setters
    // ------------------------------------------------------------------

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ExamRequest{courseId=" + courseId + ", examDate=" + examDate
                + ", title='" + title + "'}";
    }
}
