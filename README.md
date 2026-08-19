🎓 Education Management System REST API
A Production-Ready Education Management System built using Spring Boot, Spring Data JPA, Hibernate, MySQL, H2 Database, Swagger OpenAPI, JUnit, and Mockito. The system provides complete management for Students, Teachers, Courses, Enrollments, Exams, and Grades through RESTful APIs. 

📖 Table of Contents
Introduction
Features
Technology Stack
Architecture
Project Structure
Database Design
Prerequisites
Installation Guide
Database Configuration
Running the Application
Swagger API Documentation
API Modules
Student Management
Teacher Management
Course Management
Enrollment Management
Exam Management
Grade Management
CSV Upload
Scheduler
Validation Rules
Exception Handling
Testing
Build & Deployment
Future Enhancements
🚀 Introduction
The Education Management System API is a complete backend application designed for managing educational institutions.

The application allows:

Student Registration
Teacher Management
Course Assignment
Student Enrollment
Exam Scheduling
Grade Recording
Grade Analytics
CSV Grade Upload
Automated Grade Processing
Built with a clean layered architecture:

Controller
   ↓
Service
   ↓
Repository
   ↓
Database
The project follows enterprise development practices including DTO mapping, validation, exception handling, pagination, filtering, OpenAPI documentation, scheduler jobs, and automated testing. 【1-e3d136】

✨ Features
Student Management
✅ Create Student

✅ Update Student

✅ Delete Student

✅ Search Student by Name

✅ View Student Enrollments

✅ Pagination & Sorting

Teacher Management
✅ Create Teacher

✅ Update Teacher

✅ Delete Teacher

✅ Department-wise Search

✅ View Teacher Courses

✅ Business Rule Validation

Course Management
✅ Create Course

✅ Assign Teacher

✅ Reassign Teacher

✅ Search Courses

✅ View Course Students

✅ Pagination Support

Enrollment Management
✅ Student Enrollment

✅ Duplicate Enrollment Prevention

✅ Course-wise Enrollment Tracking

✅ Student Enrollment History

Exam Management
✅ Create Exam

✅ Update Exam

✅ Delete Exam

✅ Exam Date Filtering

✅ Exam Scheduling

Grade Management
✅ Record Grades

✅ Update Grades

✅ Delete Grades

✅ Grade Analytics

✅ PASS / FAIL / ABSENT Status

✅ Letter Grade Calculation

✅ Average Score Calculation

✅ CSV Upload

Additional Features
✅ Swagger Documentation

✅ Global Exception Handling

✅ Validation Framework

✅ Scheduled Tasks

✅ Unit Testing

✅ Integration Testing

✅ H2 & MySQL Profiles

✅ OpenAPI Documentation



🛠️ Technology Stack
Backend
Java 17
Spring Boot 3.2.5
Spring MVC
Spring Data JPA
Hibernate
Database
MySQL 8
H2 In-Memory Database
API Documentation
Swagger UI
OpenAPI 3
Testing
JUnit 5
Mockito
Spring Boot Test
MockMvc
Build Tool
Apache Maven


---

# 🏗️ Architecture

```text
┌─────────────────────────┐
│       Controller        │
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│        Service          │
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│       Repository        │
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│        Database         │
└─────────────────────────┘
The project follows:

Layered Architecture
Repository Pattern
DTO Pattern
Service Pattern
Dependency Injection
Transaction Management
Exception Handling Strategy
【1-e3d136】

📂 Project Structure
src/main/java/com/education/api

├── config
│   ├── CorsConfig
│   └── OpenApiConfig
│
├── controller
│   ├── StudentController
│   ├── TeacherController
│   ├── CourseController
│   ├── EnrollmentController
│   ├── ExamController
│   └── GradeController
│
├── dto
│   ├── request
│   └── response
│
├── entity
│   ├── Student
│   ├── Teacher
│   ├── Course
│   ├── Enrollment
│   ├── Exam
│   ├── Grade
│   └── GradeStatus
│
├── exception
│
├── mapper
│
├── repository
│
├── scheduler
│
├── service
│
├── service/impl
│
└── EducationApiApplication
【1-e3d136】

🗄️ Database Design
Teacher
   │
   └──────< Course
                 │
                 ├──────< Exam
                 │             │
                 │             └──────< Grade
                 │
                 └──────< Enrollment >────── Student
                                  │
                                  └──────< Grade
Relationships:

Teacher     1 -> N Courses

Course      1 -> N Exams

Student     1 -> N Enrollments

Course      1 -> N Enrollments

Enrollment  1 -> N Grades

Exam        1 -> N Grades
```【1-e3d136】

---

# ✅ Prerequisites

Install the following software before running the project.

## Java

```bash
java --version
Expected:

Java 17 or above
Maven
mvn -version
Expected:

Apache Maven 3.8+
MySQL
Install MySQL Server 8+.

Check installation:

SELECT VERSION();
📥 Project Setup
Step 1: Clone Repository
git clone https://github.com/your-username/education-api.git

cd education-api
Step 2: Install Dependencies
mvn clean install
This will:

Download dependencies
Compile source code
Execute tests
Create executable jar【1-e3d136】
⚙️ Database Configuration
MySQL Profile (Default)
application.properties

spring.application.name=education-api

server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/education_db?createDatabaseIfNotExist=true

spring.datasource.username=root

spring.datasource.password=your_Priya@02

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update

spring.jpa.show-sql=true

spring.jpa.properties.hibernate.format_sql=true

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
【1-e3d136】

Create Database
CREATE DATABASE education_db;
⚙️ H2 Profile (Development)
Run without MySQL:

mvn spring-boot:run -Dspring-boot.run.profiles=dev
Configuration:

spring.datasource.url=jdbc:h2:mem:education_db

spring.datasource.username=root

spring.datasource.password=Priya@02

spring.jpa.hibernate.ddl-auto=create-drop

spring.h2.console.enabled=true

spring.h2.console.path=/h2-console
```【1-e3d136】

---

# ▶️ Running the Application

## Run Using Maven

```bash
mvn spring-boot:run
Run Using Jar
Build:

mvn clean package
Run:

java -jar target/education-api-1.0.0.jar
Server URL

http://localhost:8080
【1-e3d136】

📖 Swagger Documentation
After server startup:

Swagger UI:

http://localhost:8080/swagger-ui.html
OpenAPI JSON:

http://localhost:8080/v3/api-docs
```【1-e3d136】

---

# 🎓 Student APIs

## Create Student

```http
POST /api/students
Request:

{
  "name": "Rahul Sharma",
  "email": "rahul@example.com",
  "enrollmentDate": "2026-01-15"
}
Get All Students
GET /api/students
Get Student By ID
GET /api/students/{id}
Update Student
PUT /api/students/{id}
Delete Student
DELETE /api/students/{id}
Student Enrollments
GET /api/students/{id}/enrollments
Search Students
GET /api/students/search?name=rah&page=0&size=10
```【1-e3d136】

---

# 👨‍🏫 Teacher APIs

## Create Teacher

```http
POST /api/teachers
{
  "name": "John Smith",
  "email": "john@example.com",
  "department": "Computer Science"
}
Get Teachers
GET /api/teachers
Get Teacher
GET /api/teachers/{id}
Update Teacher
PUT /api/teachers/{id}
Delete Teacher
DELETE /api/teachers/{id}
Teacher Courses
GET /api/teachers/{id}/courses
```【1-e3d136】

---

# 📚 Course APIs

## Create Course

```http
POST /api/courses
{
  "title": "Spring Boot",
  "description": "REST API Development",
  "teacherId": 1
}
Get Courses
GET /api/courses
Get Course
GET /api/courses/{id}
Update Course
PUT /api/courses/{id}
Delete Course
DELETE /api/courses/{id}
Course Students
GET /api/courses/{id}/students
Search Courses
GET /api/courses/search?title=spring&page=0&size=10
```【1-e3d136】

---

# 📋 Enrollment APIs

## Enroll Student

```http
POST /api/enrollments
{
  "studentId": 1,
  "courseId": 1,
  "enrollmentDate": "2026-02-01"
}
Get Enrollments
GET /api/enrollments
Get Enrollment
GET /api/enrollments/{id}
Update Enrollment
PUT /api/enrollments/{id}
Delete Enrollment
DELETE /api/enrollments/{id}
```【1-e3d136】

---

# 📝 Exam APIs

## Create Exam

```http
POST /api/exams
{
  "courseId": 1,
  "examDate": "2026-06-15",
  "title": "Mid Term"
}
Get Exams
GET /api/exams
Filter Exams
GET /api/exams?from=2026-01-01&to=2026-12-31
Update Exam
PUT /api/exams/{id}
Delete Exam
DELETE /api/exams/{id}
【1-e3d136】

📊 Grade APIs
Create Grade
POST /api/grades
{
  "enrollmentId": 1,
  "examId": 1,
  "score": 85.00,
  "status": "PASS"
}
Get Grades
GET /api/grades
Get Grade
GET /api/grades/{id}
Update Grade
PUT /api/grades/{id}
Delete Grade
DELETE /api/grades/{id}
Average Grade
GET /api/grades/exam/{examId}/average
Response:

{
  "examId": 1,
  "averageScore": 82.50
}
```【1-e3d136】

---

# 📂 CSV Upload

Endpoint:

```http
POST /api/grades/upload
Content-Type:

multipart/form-data
Sample CSV:

studentId,examId,score
1,1,85
2,1,90
3,1,76
4,1,65
Upload through:

Swagger UI
Postman
Frontend Application【1-e3d136】
⏰ Scheduler
The application contains a scheduler.

Runs every day:

@Scheduled(cron = "0 0 2 * * *")
Time:

2*00 AM
Purpose*

Find grades below passing perc*ntage *Automatically mark students as FAI【1-e3d136】
✅ Validation Ru*es
Student
Name Required
Email Required
Unique Email
*--

Teacher
Name Required
mailRequired
Unique Email
Departme*t Required
Course
Titl* Required
Teacher Must Exist
--*

Enrollment
Student Must Ex*st
Course Must Exist
No Duplic*te Enrollment
Exam
Cou*se Must Exist
Valid Exam Date
*--

Grade
Score Between 0 an* 100
One Grade Per Exam
Exam Mst Belong ToStudent Course【1-e3d136】
❌*Exception Handling
Central except*on handler:

@RestControll*rAdvice
Responses include:

*json { "timestamp": "2026-08-1310:15:30", "status": 404 "error": "Not*Found", "message": "Student not *ound",

"path": "/api/students/99" } ``*
Supported Errors*

400 Bad Request

404 Not*Found

409 Conflict

500 Internal *erver Error
```*1-e3d136】*
---

# 🧪 Testing

## Run All Tes*s

```bash
mvn test
AailableTests
Unit Tests
Stu*entServiceTest
GradeBusinessRuleTe*t
Integration Tests
StudentControllerIntegrationTes*
Tests cover:

CRUD Operati*ns
Validation Rules
Business R*les
Duplicate Prevention
API R*sponses
Grade Calculations【1-e3d*36】
📦 Build & Deployment
*Build:

mvn clean package
*``

Generated:

```text
target/edu*ation-api-1.0.0.jar
Run:

java -jar*target/education-api-1.0.0*jar
Production Profile:

java -jar target/education-api-*.0.0.jar --spring.profiles.active=*rod
🔮 Future Enhancem*nts
JWT Authentication
Spring*Security
Role-Based Access Contr*l
Docker Support
Kubernetes De*loyment
Redis Caching
Email No*ifications
Attendance Management*- Timetable Module
Dashboard Ana*ytics
Report Generation
Notifi*ation Services
 
-------*END*---------
PRIYA K N
