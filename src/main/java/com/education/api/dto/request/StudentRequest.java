package com.education.api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Incoming payload for POST /api/students and PUT /api/students/{id}.
 * Deliberately has no 'id' field - the client must never choose a primary key.
 */
public class StudentRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid address")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    private String email;

    /** Optional - defaults to today in Student.prePersist(). */
    @PastOrPresent(message = "Enrollment date cannot be in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate enrollmentDate;

    // ------------------------------------------------------------------
    //  Constructors
    // ------------------------------------------------------------------

    public StudentRequest() {
    }

    public StudentRequest(String name, String email, LocalDate enrollmentDate) {
        this.name = name;
        this.email = email;
        this.enrollmentDate = enrollmentDate;
    }

    // ------------------------------------------------------------------
    //  Getters and Setters
    // ------------------------------------------------------------------

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

    @Override
    public String toString() {
        return "StudentRequest{name='" + name + "', email='" + email
                + "', enrollmentDate=" + enrollmentDate + "}";
    }
}
