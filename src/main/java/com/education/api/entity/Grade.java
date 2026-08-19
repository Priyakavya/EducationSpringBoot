package com.education.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "grades")
@EntityListeners(AuditingEntityListener.class)
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "enrollment_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_grades_enrollment"))
    private Enrollment enrollment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exam_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_grades_exam"))
    private Exam exam;

    /** BigDecimal, never double - money and marks must not carry float error. */
    @Column(name = "score", nullable = false, precision = 5, scale = 2)
    private BigDecimal score;

    // ENUM ADDED HERE
    @Enumerated(EnumType.STRING) // Saves "PASS" in DB instead of 0/1/2
    @Column(name = "status", nullable = false)
    private GradeStatus status = GradeStatus.PASS;

    // ------------------------------------------------------------------
    //  Constructors
    // ------------------------------------------------------------------
    public Grade() {
    }

    public Grade(Enrollment enrollment, Exam exam, BigDecimal score) {
        this.enrollment = enrollment;
        this.exam = exam;
        this.score = score;
        setStatusFromScore(); // auto set PASS/FAIL
    }

    public Grade(Long id, Enrollment enrollment, Exam exam, BigDecimal score) {
        this.id = id;
        this.enrollment = enrollment;
        this.exam = exam;
        this.score = score;
        setStatusFromScore(); // auto set PASS/FAIL
    }

    // ------------------------------------------------------------------
    //  Getters and Setters
    // ------------------------------------------------------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
        setStatusFromScore(); // auto update status when score changes
    }

    public GradeStatus getStatus() {
        return status;
    }

    public void setStatus(GradeStatus status) {
        this.status = status;
    }

    // ------------------------------------------------------------------
    //  Helper method
    // ------------------------------------------------------------------
    private void setStatusFromScore() {
        if (this.score == null) {
            this.status = GradeStatus.ABSENT;
        } else if (this.score.doubleValue() >= 35) {
            this.status = GradeStatus.PASS;
        } else {
            this.status = GradeStatus.FAIL;
        }
    }

    /** Letter grade derived from the numeric score. */
    @Transient
    public String getLetterGrade() {
        if (score == null) {
            return null;
        }
        double value = score.doubleValue();
        if (value >= 90) return "A";
        if (value >= 80) return "B";
        if (value >= 70) return "C";
        if (value >= 60) return "D";
        if (value >= 35) return "E";
        return "F";
    }
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // GETTERS
    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }

    public LocalDateTime getUpdatedAt() { 
        return updatedAt; 
    }

}