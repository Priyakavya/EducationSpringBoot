package com.education.api.dto.response;

/**
 * Flattens the teacher into id + name. Returning the whole Teacher entity here
 * is what causes the classic infinite-recursion stack overflow in JPA APIs.
 */
public class CourseResponse {

    private Long id;
    private String title;
    private String description;
    private Long teacherId;
    private String teacherName;

    public CourseResponse() {
    }

    public CourseResponse(Long id, String title, String description, Long teacherId, String teacherName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
    }

    // ---------------- Getters and Setters ----------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
