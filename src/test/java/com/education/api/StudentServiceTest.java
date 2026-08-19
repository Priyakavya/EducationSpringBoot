package com.education.api;
import com.education.api.dto.request.StudentRequest; 
import com.education.api.dto.response.StudentResponse;
import com.education.api.entity.Student;
import com.education.api.mapper.StudentMapper;
import com.education.api.repository.StudentRepository;
import com.education.api.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper; 

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    public void testCreateStudent_Success() {
        // 1. ARRANGE
        StudentRequest request = new StudentRequest();
        request.setEmail("rahul@test.com");
        request.setName("Rahul");

        Student studentEntity = new Student();
        studentEntity.setId(1L);
        studentEntity.setName("Rahul");
        studentEntity.setEmail("rahul@test.com");

        StudentResponse responseDto = new StudentResponse();
        responseDto.setId(1L);
        responseDto.setName("Rahul");
        responseDto.setEmail("rahul@test.com");

        // Mock behavior
        when(studentRepository.findByEmail("rahul@test.com")).thenReturn(Optional.empty()); 
        when(studentMapper.toEntity(request)).thenReturn(studentEntity);
        when(studentRepository.save(any(Student.class))).thenReturn(studentEntity);
        when(studentMapper.toResponse(studentEntity)).thenReturn(responseDto);

        // 2. ACT
        StudentResponse result = studentService.create(request);

        // 3. ASSERT
        assertNotNull(result);
        assertEquals("Rahul", result.getName());
        assertEquals("rahul@test.com", result.getEmail());
        verify(studentRepository, times(1)).save(any(Student.class));
    }
}