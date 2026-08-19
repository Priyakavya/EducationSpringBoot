package com.education.api.service;

import com.education.api.dto.request.StudentRequest;
import com.education.api.dto.response.StudentResponse;
import com.education.api.dto.response.EnrollmentResponse;
import org.springframework.data.domain.Page;      
import org.springframework.data.domain.Pageable;  
import org.springframework.data.domain.PageRequest; 
import org.springframework.data.domain.Sort;      

import java.util.List;

public interface StudentService {

    StudentResponse create(StudentRequest request);

    List<StudentResponse> findAll(String nameFilter);

    StudentResponse findById(Long id);

    StudentResponse update(Long id, StudentRequest request);

    void delete(Long id);

    /** Extra: GET /api/students/{id}/enrollments */
    List<EnrollmentResponse> findEnrollmentsByStudentId(Long studentId);
    Page<StudentResponse> searchStudentsByName(String name, Pageable pageable);
  
}
