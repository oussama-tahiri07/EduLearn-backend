package com.example.eduLearn.controller;

import com.example.eduLearn.service.SupabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/modules")
public class ModuleController {

    @Autowired
    private SupabaseService supabaseService;

    @GetMapping
    public String getModules() {
        return supabaseService.getModules();
    }

    @PostMapping
    public String addModule(@RequestBody String jsonBody) {
        return supabaseService.addModule(jsonBody);
    }
}
