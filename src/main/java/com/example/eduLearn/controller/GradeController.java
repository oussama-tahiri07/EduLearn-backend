package com.example.eduLearn.controller;

import com.example.eduLearn.model.Grade;
import com.example.eduLearn.service.SupabaseGradeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // Create
    @PostMapping
    public ResponseEntity<Grade> createGrade(
            @RequestBody Grade grade,
            @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        Grade createdGrade = gradeService.createGrade(grade, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGrade);
    }

    // Read (All for user)
    @GetMapping
    public List<Grade> getGrades(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return gradeService.getGradesByUser(userId);
    }

    // Read (Single grade)
    @GetMapping("/{id}")
    public ResponseEntity<Grade> getGrade(
            @PathVariable String id,
            @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        Grade grade = gradeService.getGradeById(id, userId);
        return ResponseEntity.ok(grade);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Grade> updateGrade(
            @PathVariable String id,
            @RequestBody Grade gradeDetails,
            @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        Grade updatedGrade = gradeService.updateGrade(id, gradeDetails, userId);
        return ResponseEntity.ok(updatedGrade);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrade(
            @PathVariable String id,
            @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        gradeService.deleteGrade(id, userId);
        return ResponseEntity.noContent().build();
    }

    // Admin endpoint to get all grades (for teachers/admins)
    @GetMapping("/admin/all")
    public ResponseEntity<List<Grade>> getAllGrades(
            @AuthenticationPrincipal Jwt jwt) {
        // In a real app, verify user has admin/teacher role here
        List<Grade> grades = gradeService.getAllGrades();
        return ResponseEntity.ok(grades);
    }
}