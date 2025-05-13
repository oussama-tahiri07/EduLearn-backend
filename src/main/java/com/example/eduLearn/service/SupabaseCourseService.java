package com.example.eduLearn.service;

import com.example.eduLearn.model.Course;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class SupabaseCourseService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public Course createCourse(Course classData, String teacherId) {
        String url = supabaseUrl + "/rest/v1/classes";
        
        classData.setId(UUID.randomUUID().toString());
        classData.setTeacherId(teacherId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseKey);
        headers.set("Authorization", "Bearer " + supabaseKey);
        headers.set("Prefer", "return=representation");

        HttpEntity<Course> request = new HttpEntity<>(classData, headers);
        ResponseEntity<Course[]> response = restTemplate.exchange(
            url, HttpMethod.POST, request, Course[].class);
        
        return response.getBody()[0];
    }

    public List<Course> getCourseByTeacher(String teacherId) {
        String url = supabaseUrl + "/rest/v1/classes?teacher_id=eq." + teacherId;
        ResponseEntity<Course[]> response = restTemplate.exchange(
            url, HttpMethod.GET, new HttpEntity<>(createHeaders()), Course[].class);
        return Arrays.asList(response.getBody());
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseKey);
        headers.set("Authorization", "Bearer " + supabaseKey);
        return headers;
    }
}