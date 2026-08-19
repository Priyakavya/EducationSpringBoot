package com.education.api.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The join entity that resolves the many-to-many between Student and Course.
 * It is a first-class entity (not a plain @JoinTable) because it carries its
 * own data - enrollment_date - and because Grade points at it.
 *
 * Relationships:
 *   Enrollment * ---- 1 Student
 *   Enrollment * ---- 1 Course
 *   Enrollment 1 ---- * Grade
 */
@Entity
@Table(
        name = "enrollments",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_enrollments_student_course",
                columnNames = {"student_id", "course_id"}
        )
)
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "student_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_enrollments_student")
    )
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "course_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_enrollments_course")
    )
    private Course course;

    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Grade> grades = new ArrayList<>();

    // ------------------------------------------------------------------
    //  Constructors
    // ------------------------------------------------------------------

    public Enrollment() {
    }

    public Enrollment(Student student, Course course, LocalDate enrollmentDate) {
        this.student = student;
        this.course = course;
        this.enrollmentDate = enrollmentDate;
    }

    public Enrollment(Long id, Student student, Course course, LocalDate enrollmentDate) {
        this.id = id;
        this.student = student;
        this.course = course;
        this.enrollmentDate = enrollmentDate;
    }

    // ------------------------------------------------------------------
    //  Lifecycle callback
    // ------------------------------------------------------------------

    @PrePersist
    public void prePersist() {
        if (this.enrollmentDate == null) {
            this.enrollmentDate = LocalDate.now();
        }
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

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
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
        grade.setEnrollment(this);
    }

    public void removeGrade(Grade grade) {
        this.grades.remove(grade);
        grade.setEnrollment(null);
    }

    // ------------------------------------------------------------------
    //  equals / hashCode / toString
    // ------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Enrollment)) return false;
        Enrollment other = (Enrollment) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass().hashCode());
    }

    @Override
    public String toString() {
        return "Enrollment{id=" + id
                + ", studentId=" + (student != null ? student.getId() : null)
                + ", courseId=" + (course != null ? course.getId() : null)
                + ", enrollmentDate=" + enrollmentDate + "}";
    }
}
