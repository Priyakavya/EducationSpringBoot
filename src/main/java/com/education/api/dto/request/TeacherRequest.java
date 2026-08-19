package com.education.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TeacherRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid address")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    private String email;

    @NotBlank(message = "Department is required")
    @Size(max = 100, message = "Department must not exceed 100 characters")
    private String department;

    // ------------------------------------------------------------------
    //  Constructors
    // ------------------------------------------------------------------

    public TeacherRequest() {
    }

    public TeacherRequest(String name, String email, String department) {
        this.name = name;
        this.email = email;
        this.department = department;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "TeacherRequest{name='" + name + "', email='" + email
                + "', department='" + department + "'}";
    }
}
