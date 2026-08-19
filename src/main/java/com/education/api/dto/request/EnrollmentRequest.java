package com.education.api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class EnrollmentRequest {

    @NotNull(message = "studentId is required")
    @Positive(message = "studentId must be a positive number")
    private Long studentId;

    @NotNull(message = "courseId is required")
    @Positive(message = "courseId must be a positive number")
    private Long courseId;

    /** Optional - defaults to today in Enrollment.prePersist(). */
    @PastOrPresent(message = "Enrollment date cannot be in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate enrollmentDate;

    // ------------------------------------------------------------------
    //  Constructors
    // ------------------------------------------------------------------

    public EnrollmentRequest() {
    }

    public EnrollmentRequest(Long studentId, Long courseId, LocalDate enrollmentDate) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrollmentDate = enrollmentDate;
    }

    // ------------------------------------------------------------------
    //  Getters and Setters
    // ------------------------------------------------------------------

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    @Override
    public String toString() {
        return "EnrollmentRequest{studentId=" + studentId + ", courseId=" + courseId
                + ", enrollmentDate=" + enrollmentDate + "}";
    }
}
