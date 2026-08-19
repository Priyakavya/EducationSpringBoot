package com.education.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * The client sends teacherId (a flat number), never a nested Teacher object.
 * The service resolves it into a managed Teacher entity.
 */
public class CourseRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 150, message = "Title must be between 3 and 150 characters")
    private String title;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @NotNull(message = "teacherId is required")
    @Positive(message = "teacherId must be a positive number")
    private Long teacherId;

    // ------------------------------------------------------------------
    //  Constructors
    // ------------------------------------------------------------------

    public CourseRequest() {
    }

    public CourseRequest(String title, String description, Long teacherId) {
        this.title = title;
        this.description = description;
        this.teacherId = teacherId;
    }

    // ------------------------------------------------------------------
    //  Getters and Setters
    // ------------------------------------------------------------------

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

    @Override
    public String toString() {
        return "CourseRequest{title='" + title + "', teacherId=" + teacherId + "}";
    }
}
