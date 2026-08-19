package com.education.api.dto.response;

import java.math.BigDecimal;

import com.education.api.entity.GradeStatus;

/**
 * Denormalised on purpose: a marks sheet needs the student name and exam title,
 * and forcing the UI to make three more calls to assemble one row is bad design.
 */
public class GradeResponse {

    private Long id;
    private Long enrollmentId;
    private Long studentId;
    private String studentName;
    private Long examId;
    private String examTitle;
    private String courseTitle;
    private BigDecimal score;
    private String letterGrade;
    private GradeStatus status;
   

    public GradeResponse() {
    }

    public GradeResponse(Long id, Long enrollmentId, Long studentId, String studentName,
                         Long examId, String examTitle, String courseTitle,
                         BigDecimal score, String letterGrade) {
        this.id = id;
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.examId = examId;
        this.examTitle = examTitle;
        this.courseTitle = courseTitle;
        this.score = score;
        this.letterGrade = letterGrade;
    }

    // ---------------- Getters and Setters ----------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public String getExamTitle() {
        return examTitle;
    }

    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public void setLetterGrade(String letterGrade) {
        this.letterGrade = letterGrade;
    }
    public GradeStatus getStatus() {
        return status;
    }

    public void setStatus(GradeStatus status) {
        this.status = status;
    }
}
