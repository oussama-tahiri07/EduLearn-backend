package com.example.eduLearn.service;

import com.example.eduLearn.model.Grade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class SupabaseGradeService implements GradeService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Grade createGrade(Grade grade, String userId) {
        String url = supabaseUrl + "/rest/v1/grades";
        
        // Set required fields
        grade.setId(UUID.randomUUID().toString()); // Generate UUID if not set
        grade.setUserId(userId);
        
        HttpHeaders headers = createHeaders();
        headers.set("Prefer", "return=representation");
        HttpEntity<Grade> entity = new HttpEntity<>(grade, headers);
        
        ResponseEntity<Grade[]> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            entity,
            Grade[].class
        );
        
        return response.getBody() != null && response.getBody().length > 0 ? 
               response.getBody()[0] : null;
    }

    @Override
    public List<Grade> getGradesByUser(String userId) {
        String url = String.format("%s/rest/v1/grades?user_id=eq.%s&select=*", 
                      supabaseUrl, userId);
        return executeGetRequest(url);
    }

    @Override
    public Grade getGradeById(String id, String userId) {
        String url = String.format("%s/rest/v1/grades?id=eq.%s&user_id=eq.%s&select=*", 
                      supabaseUrl, id, userId);
        List<Grade> grades = executeGetRequest(url);
        return grades.isEmpty() ? null : grades.get(0);
    }

    @Override
    public Grade updateGrade(String id, Grade gradeDetails, String userId) {
        String url = String.format("%s/rest/v1/grades?id=eq.%s&user_id=eq.%s", 
                      supabaseUrl, id, userId);
        
        // Create update payload with only modifiable fields
        Grade updateData = new Grade();
        updateData.setScore(gradeDetails.getScore());
        updateData.setFeedback(gradeDetails.getFeedback());
        updateData.setSubjectName(gradeDetails.getSubjectName());
        
        HttpHeaders headers = createHeaders();
        headers.set("Prefer", "return=representation");
        HttpEntity<Grade> entity = new HttpEntity<>(updateData, headers);
        
        ResponseEntity<Grade[]> response = restTemplate.exchange(
            url,
            HttpMethod.PATCH,
            entity,
            Grade[].class
        );
        
        return response.getBody() != null && response.getBody().length > 0 ? 
               response.getBody()[0] : null;
    }

    @Override
    public void deleteGrade(String id, String userId) {
        String url = String.format("%s/rest/v1/grades?id=eq.%s&user_id=eq.%s", 
                      supabaseUrl, id, userId);
        
        HttpEntity<String> entity = new HttpEntity<>(createHeaders());
        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }

    // In SupabaseGradeService implementation
    @Override
    public List<Grade> getGradesBySubject(String subjectCode, String userId) {
        String url = String.format("%s/rest/v1/grades?subject_code=eq.%s&user_id=eq.%s&select=*", 
                      supabaseUrl, subjectCode, userId);
        return executeGetRequest(url);
    }

    @Override
    public List<Grade> getAllGrades() {
        String url = supabaseUrl + "/rest/v1/grades?select=*";
        return executeGetRequest(url);
    }

    private List<Grade> executeGetRequest(String url) {
        HttpEntity<String> entity = new HttpEntity<>(createHeaders());
        ResponseEntity<Grade[]> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            Grade[].class
        );
        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseKey);
        headers.set("Authorization", "Bearer " + supabaseKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", "application/vnd.pgrst.object+json");
        return headers;
    }
}