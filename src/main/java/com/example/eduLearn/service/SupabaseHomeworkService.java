package com.example.eduLearn.service;

import com.example.eduLearn.model.Homework;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class SupabaseHomeworkService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public Homework createHomework(Homework homework, String teacherId) {
        String url = supabaseUrl + "/rest/v1/homeworks";
        
        homework.setId(UUID.randomUUID().toString());

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseKey);
        headers.set("Authorization", "Bearer " + supabaseKey);
        headers.set("Prefer", "return=representation");

        HttpEntity<Homework> request = new HttpEntity<>(homework, headers);
        ResponseEntity<Homework[]> response = restTemplate.exchange(
            url, HttpMethod.POST, request, Homework[].class);
        
        return response.getBody()[0];
    }

    public List<Homework> getHomeworksByCourse(String classId) {
        String url = supabaseUrl + "/rest/v1/homeworks?courses_id=eq." + classId;
        ResponseEntity<Homework[]> response = restTemplate.exchange(
            url, HttpMethod.GET, new HttpEntity<>(createHeaders()), Homework[].class);
        return Arrays.asList(response.getBody());
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseKey);
        headers.set("Authorization", "Bearer " + supabaseKey);
        return headers;
    }
}