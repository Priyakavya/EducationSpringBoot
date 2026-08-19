package com.education.api.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An exam scheduled against a course.
 *
 * Relationships:
 *   Exam * ---- 1 Course
 *   Exam 1 ---- * Grade
 */
@Entity
@Table(name = "exams")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "course_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_exams_course")
    )
    private Course course;

    @Column(name = "exam_date", nullable = false)
    private LocalDate examDate;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Grade> grades = new ArrayList<>();

    // ------------------------------------------------------------------
    //  Constructors
    // ------------------------------------------------------------------

    public Exam() {
    }

    public Exam(Course course, LocalDate examDate, String title) {
        this.course = course;
        this.examDate = examDate;
        this.title = title;
    }

    public Exam(Long id, Course course, LocalDate examDate, String title) {
        this.id = id;
        this.course = course;
        this.examDate = examDate;
        this.title = title;
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    // ------------------------------------------------------------------
    //  Bi-directional helpers
    // ------------------------------------------------------------------

    public void addGrade(Grade grade) {
        this.grades.add(grade);
        grade.setExam(this);
    }

    public void removeGrade(Grade grade) {
        this.grades.remove(grade);
        grade.setExam(null);
    }

    // ------------------------------------------------------------------
    //  equals / hashCode / toString
    // ------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exam)) return false;
        Exam other = (Exam) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass().hashCode());
    }

    @Override
    public String toString() {
        return "Exam{id=" + id + ", title='" + title + "', examDate=" + examDate
                + ", courseId=" + (course != null ? course.getId() : null) + "}";
    }
}
