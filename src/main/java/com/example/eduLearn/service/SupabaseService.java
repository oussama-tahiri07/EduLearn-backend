package com.example.eduLearn.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SupabaseService {

    @Value("${supabase.url}")
    private String SUPABASE_URL;

    @Value("${supabase.key}")
    private String SUPABASE_KEY;

    private final RestTemplate restTemplate = new RestTemplate();

    // GET all modules
    public String getModules() {
        String url = SUPABASE_URL + "/modules";

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", SUPABASE_KEY);
        headers.set("Authorization", "Bearer " + SUPABASE_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody();
    }

    // ADD a new module
    public String addModule(String jsonBody) {
        String url = SUPABASE_URL + "/modules";

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", SUPABASE_KEY);
        headers.set("Authorization", "Bearer " + SUPABASE_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        return response.getBody();
    }
}
