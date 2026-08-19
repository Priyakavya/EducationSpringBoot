package com.education.api.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A student enrolled in the institution.
 *
 * Relationships:
 *   Student 1 ---- * Enrollment   (a student can enrol in many courses)
 */
@Entity
@Table(
        name = "students",
        uniqueConstraints = @UniqueConstraint(name = "uk_students_email", columnNames = "email")
)
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    /*
     * mappedBy -> Enrollment.student owns the FK column.
     * orphanRemoval + CascadeType.ALL: deleting a student removes their enrolments.
     * LAZY so listing students does not drag every enrolment into memory.
     */
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Enrollment> enrollments = new ArrayList<>();

    // ------------------------------------------------------------------
    //  Constructors
    // ------------------------------------------------------------------

    /** JPA requires a no-arg constructor. */
    public Student() {
    }

    public Student(String name, String email, LocalDate enrollmentDate) {
        this.name = name;
        this.email = email;
        this.enrollmentDate = enrollmentDate;
    }

    public Student(Long id, String name, String email, LocalDate enrollmentDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.enrollmentDate = enrollmentDate;
    }

    // ------------------------------------------------------------------
    //  Lifecycle callback
    // ------------------------------------------------------------------

    /** Default the enrolment date to today when the client did not send one. */
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

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    // ------------------------------------------------------------------
    //  Bi-directional helpers - keep both sides of the link in sync
    // ------------------------------------------------------------------

    public void addEnrollment(Enrollment enrollment) {
        this.enrollments.add(enrollment);
        enrollment.setStudent(this);
    }

    public void removeEnrollment(Enrollment enrollment) {
        this.enrollments.remove(enrollment);
        enrollment.setStudent(null);
    }

    // ------------------------------------------------------------------
    //  equals / hashCode / toString
    // ------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student other = (Student) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass().hashCode());
    }

    /** Never print the collection here - it would trigger a lazy load / infinite loop. */
    @Override
    public String toString() {
        return "Student{id=" + id + ", name='" + name + "', email='" + email
                + "', enrollmentDate=" + enrollmentDate + "}";
    }
}
