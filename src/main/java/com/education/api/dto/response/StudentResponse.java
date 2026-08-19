package com.education.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

/** Outgoing shape for every student endpoint. */
public class StudentResponse {

    private Long id;
    private String name;
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate enrollmentDate;

    /** Convenience count so a dashboard does not need a second call. */
    private Integer totalEnrollments;

    public StudentResponse() {
    }

    public StudentResponse(Long id, String name, String email, LocalDate enrollmentDate, Integer totalEnrollments) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.enrollmentDate = enrollmentDate;
        this.totalEnrollments = totalEnrollments;
    }

    // ---------------- Getters and Setters ----------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public Integer getTotalEnrollments() {
        return totalEnrollments;
    }

    public void setTotalEnrollments(Integer totalEnrollments) {
        this.totalEnrollments = totalEnrollments;
    }
}
