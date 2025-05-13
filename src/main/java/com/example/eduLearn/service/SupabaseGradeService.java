package com.example.eduLearn.service;

import com.example.eduLearn.exception.ResourceNotFoundException;
import com.example.eduLearn.model.Grade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class SupabaseGradeService implements GradeService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    private final RestTemplate restTemplate = new RestTemplate();


    // ================= CORE GRADE OPERATIONS ================= //

    @Override
    public Grade submitGrade(Grade grade, String teacherId) {
        // Validate required fields
        if (grade.getStudentId() == null || grade.getClassId() == null) {
            throw new IllegalArgumentException("Student ID and Course ID are required");
        }

        String url = supabaseUrl + "/rest/v1/grades";
        
        // Set immutable fields
        grade.setId(UUID.randomUUID().toString());
        grade.setTeacherId(teacherId);       // From authenticated teacher
        grade.setGradedAt(LocalDateTime.now());

        HttpHeaders headers = createAuthHeaders();
        headers.set("Prefer", "return=representation");

        HttpEntity<Grade> request = new HttpEntity<>(grade, headers);
        ResponseEntity<Grade[]> response = restTemplate.exchange(
            url, HttpMethod.POST, request, Grade[].class);
        
        return response.getBody()[0];
    }

    @Override
    public Grade updateGrade(String gradeId, Grade gradeDetails, String teacherId) {
        String url = String.format("%s/rest/v1/grades?id=eq.%s&teacher_id=eq.%s",
                         supabaseUrl, gradeId, teacherId);
        
        HttpHeaders headers = createAuthHeaders();
        headers.set("Prefer", "return=representation");
        
        HttpEntity<Grade> request = new HttpEntity<>(gradeDetails, headers);
        ResponseEntity<Grade[]> response = restTemplate.exchange(
            url, HttpMethod.PATCH, request, Grade[].class);
        
        return response.getBody()[0];
    }

    // ================= ROLE-BASED ACCESS METHODS ================= //

    @Override
    public List<Grade> getGradesByStudent(String studentId) {
        String url = String.format("%s/rest/v1/grades?student_id=eq.%s", 
                         supabaseUrl, studentId);
        return executeGetRequest(url);
    }

    @Override
    public List<Grade> getGradesByClass(String classId, String teacherId) {
        String url = String.format("%s/rest/v1/grades?class_id=eq.%s&teacher_id=eq.%s",
                         supabaseUrl, classId, teacherId);
        return executeGetRequest(url);
    }

    @Override
    public List<Grade> getGradesBySubject(String subjectCode, String teacherId) {
        String url = String.format("%s/rest/v1/grades?subject_code=eq.%s&teacher_id=eq.%s",
                         supabaseUrl, subjectCode, teacherId);
        return executeGetRequest(url);
    }

    // ================= PRIVATE UTILITIES ================= //

    private List<Grade> executeGetRequest(String url) {
        ResponseEntity<Grade[]> response = restTemplate.exchange(
            url, HttpMethod.GET, new HttpEntity<>(createAuthHeaders()), Grade[].class);
        return Arrays.asList(response.getBody());
    }

    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseKey);
        headers.set("Authorization", "Bearer " + supabaseKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }


    @Override
    public Grade getGradeById(String id, String userId) {
        String url = String.format("%s/rest/v1/grades?id=eq.%s&user_id=eq.%s&select=*", 
                      supabaseUrl, id, userId);
        List<Grade> grades = executeGetRequest(url);
        return grades.isEmpty() ? null : grades.get(0);
    }

    @Override
    public void deleteGrade(String gradeId) {
        // Admin version - no restrictions
        String url = String.format("%s/rest/v1/grades?id=eq.%s", supabaseUrl, gradeId);
        
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders());
        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }

    @Override
    public void deleteGradeByTeacher(String gradeId, String teacherId) throws Exception {
        // Teacher version - must verify ownership
        String url = String.format(
            "%s/rest/v1/grades?id=eq.%s&teacher_id=eq.%s", 
            supabaseUrl, gradeId, teacherId
        );
        
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders());
        ResponseEntity<Void> response = restTemplate.exchange(
            url, HttpMethod.DELETE, entity, Void.class);
        
        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new ResourceNotFoundException(
            		"Grade not found or you don't have permission");
        }
    }

    @Override
    public List<Grade> getAllGrades() {
        String url = supabaseUrl + "/rest/v1/grades?select=*";
        return executeGetRequest(url);
    }
}