package com.education.api.dto.response;

public class TeacherResponse {

    private Long id;
    private String name;
    private String email;
    private String department;
    private Integer totalCourses;

    public TeacherResponse() {
    }

    public TeacherResponse(Long id, String name, String email, String department, Integer totalCourses) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.totalCourses = totalCourses;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getTotalCourses() {
        return totalCourses;
    }

    public void setTotalCourses(Integer totalCourses) {
        this.totalCourses = totalCourses;
    }
}
