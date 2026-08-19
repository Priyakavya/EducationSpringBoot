package com.education.api.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A teacher who owns one or more courses.
 *
 * Relationships:
 *   Teacher 1 ---- * Course
 */
@Entity
@Table(
        name = "teachers",
        uniqueConstraints = @UniqueConstraint(name = "uk_teachers_email", columnNames = "email")
)
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @Column(name = "department", nullable = false, length = 100)
    private String department;

    /*
     * No cascade delete here on purpose: deleting a teacher who still owns
     * courses is blocked in TeacherServiceImpl with a 409 Conflict, which is
     * friendlier than silently destroying course data.
     */
    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    private List<Course> courses = new ArrayList<>();

    // ------------------------------------------------------------------
    //  Constructors
    // ------------------------------------------------------------------

    public Teacher() {
    }

    public Teacher(String name, String email, String department) {
        this.name = name;
        this.email = email;
        this.department = department;
    }

    public Teacher(Long id, String name, String email, String department) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    // ------------------------------------------------------------------
    //  Bi-directional helpers
    // ------------------------------------------------------------------

    public void addCourse(Course course) {
        this.courses.add(course);
        course.setTeacher(this);
    }

    public void removeCourse(Course course) {
        this.courses.remove(course);
        course.setTeacher(null);
    }

    // ------------------------------------------------------------------
    //  equals / hashCode / toString
    // ------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Teacher)) return false;
        Teacher other = (Teacher) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass().hashCode());
    }

    @Override
    public String toString() {
        return "Teacher{id=" + id + ", name='" + name + "', email='" + email
                + "', department='" + department + "'}";
    }
}
