package com.education.api;

import com.education.api.dto.request.StudentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * End-to-end tests through the real HTTP stack against the H2 dev database.
 *
 * @Transactional rolls each test back afterwards, so tests cannot pollute
 * each other's data.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@Transactional
class StudentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createStudent_returns201_withLocationHeader() throws Exception {
        StudentRequest request = new StudentRequest(
                "Test Student", "test.student@example.com", LocalDate.of(2026, 1, 15));

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Test Student"))
                .andExpect(jsonPath("$.email").value("test.student@example.com"));
    }

    @Test
    void createStudent_withBlankName_returns400_withFieldErrors() throws Exception {
        StudentRequest request = new StudentRequest("", "bad", null);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.validationErrors.name").exists())
                .andExpect(jsonPath("$.validationErrors.email").exists());
    }

    @Test
    void createStudent_withDuplicateEmail_returns409() throws Exception {
        // This email is already in data-h2.sql
        StudentRequest request = new StudentRequest(
                "Impostor", "vishnu.vardhan@student.edu.in", LocalDate.of(2026, 1, 1));

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void getStudentById_whenMissing_returns404() throws Exception {
        mockMvc.perform(get("/api/students/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(
                        org.hamcrest.Matchers.containsString("Student")));
    }

    @Test
    void getAllStudents_returnsSeededRows() throws Exception {
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(
                        org.hamcrest.Matchers.greaterThanOrEqualTo(10)));
    }

    @Test
    void deleteStudent_returns204_thenGetReturns404() throws Exception {
        mockMvc.perform(delete("/api/students/10"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/students/10"))
                .andExpect(status().isNotFound());
    }
}
