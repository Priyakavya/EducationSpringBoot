package com.education.api.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A course taught by exactly one teacher.
 *
 * Relationships:
 *   Course * ---- 1 Teacher
 *   Course 1 ---- * Enrollment
 *   Course 1 ---- * Exam
 */
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column
    private String fileurl;

    /*
     * The owning side: this class holds the teacher_id FK column.
     * LAZY + a DTO mapper is what stops Jackson recursing Course -> Teacher -> Course.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "teacher_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_courses_teacher")
    )
    private Teacher teacher;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Enrollment> enrollments = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Exam> exams = new ArrayList<>();

    // ------------------------------------------------------------------
    //  Constructors
    // ------------------------------------------------------------------

    public Course() {
    }

    public Course(String title, String description, Teacher teacher) {
        this.title = title;
        this.description = description;
        this.teacher = teacher;
    }

    public Course(Long id, String title, String description, Teacher teacher) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.teacher = teacher;
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

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }

    // ------------------------------------------------------------------
    //  Bi-directional helpers
    // ------------------------------------------------------------------

    public void addExam(Exam exam) {
        this.exams.add(exam);
        exam.setCourse(this);
    }

    public void removeExam(Exam exam) {
        this.exams.remove(exam);
        exam.setCourse(null);
    }

    public void addEnrollment(Enrollment enrollment) {
        this.enrollments.add(enrollment);
        enrollment.setCourse(this);
    }

    public void removeEnrollment(Enrollment enrollment) {
        this.enrollments.remove(enrollment);
        enrollment.setCourse(null);
    }

    // ------------------------------------------------------------------
    //  equals / hashCode / toString
    // ------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course other = (Course) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass().hashCode());
    }

    @Override
    public String toString() {
        return "Course{id=" + id + ", title='" + title + "', teacherId="
                + (teacher != null ? teacher.getId() : null) + "}";
    }
}
