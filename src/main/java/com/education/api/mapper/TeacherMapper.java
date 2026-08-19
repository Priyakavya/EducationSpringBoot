package com.education.api.mapper;

import com.education.api.dto.request.TeacherRequest;
import com.education.api.dto.response.TeacherResponse;
import com.education.api.entity.Teacher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TeacherMapper {

    public Teacher toEntity(TeacherRequest request) {
        Teacher teacher = new Teacher();
        teacher.setName(request.getName());
        teacher.setEmail(request.getEmail());
        teacher.setDepartment(request.getDepartment());
        return teacher;
    }

    public void updateEntity(Teacher teacher, TeacherRequest request) {
        teacher.setName(request.getName());
        teacher.setEmail(request.getEmail());
        teacher.setDepartment(request.getDepartment());
    }

    public TeacherResponse toResponse(Teacher teacher) {
        TeacherResponse response = new TeacherResponse();
        response.setId(teacher.getId());
        response.setName(teacher.getName());
        response.setEmail(teacher.getEmail());
        response.setDepartment(teacher.getDepartment());
        response.setTotalCourses(teacher.getCourses() != null ? teacher.getCourses().size() : 0);
        return response;
    }

    public List<TeacherResponse> toResponseList(List<Teacher> teachers) {
        return teachers.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
