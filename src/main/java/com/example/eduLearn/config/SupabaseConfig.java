package com.example.eduLearn.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SupabaseConfig {

    @Value("${supabase.api.url}")
    public String SUPABASE_API_URL;

    @Value("${supabase.anon.key}")
    public String SUPABASE_ANON_KEY;
}
