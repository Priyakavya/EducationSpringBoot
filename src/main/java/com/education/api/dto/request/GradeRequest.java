package com.education.api.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

import com.education.api.entity.GradeStatus;

public class GradeRequest {

    @NotNull(message = "enrollmentId is required")
    @Positive(message = "enrollmentId must be a positive number")
    private Long enrollmentId;

    @NotNull(message = "examId is required")
    @Positive(message = "examId must be a positive number")
    private Long examId;

    @NotNull(message = "score is required")
    @DecimalMin(value = "0.00", message = "Score cannot be negative")
    @DecimalMax(value = "100.00", message = "Score cannot exceed 100")
    @Digits(integer = 3, fraction = 2, message = "Score must have at most 2 decimal places")
    private BigDecimal score;
    @NotNull(message = "status is required")
    private GradeStatus status;

    // ------------------------------------------------------------------
    //  Constructors
    // ------------------------------------------------------------------

    public GradeRequest() {
    }

    public GradeRequest(Long enrollmentId, Long examId, BigDecimal score, GradeStatus status) {

        this.enrollmentId = enrollmentId;
        this.examId = examId;
        this.score = score;
        this.status = status;
    }

    // ------------------------------------------------------------------
    //  Getters and Setters
    // ------------------------------------------------------------------

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }
    public GradeStatus getStatus() {
    	return status;
   
    }
    public void setStatus(GradeStatus status) {
    	this.status = status;
    }


    @Override
    public String toString() {
        return "GradeRequest{enrollmentId=" + enrollmentId + ", examId=" + examId
                + ", score=" + score + "}";
    }
}
