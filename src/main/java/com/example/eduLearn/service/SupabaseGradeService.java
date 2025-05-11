package com.example.eduLearn.service;

import com.example.eduLearn.model.Grade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class SupabaseGradeService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Grade> getGradesByUser(String userId) {
        String url = supabaseUrl + "/rest/v1/grades?user_id=eq." + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseKey);
        headers.set("Authorization", "Bearer " + supabaseKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Grade[]> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            Grade[].class
        );

        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }
}