package com.example.eduLearn.controller;

import com.example.eduLearn.model.Grade;
import com.example.eduLearn.service.SupabaseGradeService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    private final SupabaseGradeService gradeService;

    public GradeController(SupabaseGradeService gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping
    public List<Grade> getGrades(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject(); // Supabase user ID from JWT
        return gradeService.getGradesByUser(userId);
    }
}