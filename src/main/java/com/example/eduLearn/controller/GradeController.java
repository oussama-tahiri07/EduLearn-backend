package com.example.eduLearn.controller;

import com.example.eduLearn.model.Grade;
import com.example.eduLearn.service.SupabaseGradeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    // ================= TEACHER ENDPOINTS ================= //
    @PostMapping
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<Grade> submitGrade(
            @RequestBody Grade grade,
            @AuthenticationPrincipal Jwt jwt) {
        String teacherId = jwt.getSubject();
        Grade createdGrade = gradeService.submitGrade(grade, teacherId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGrade);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<Grade> updateGrade(
            @PathVariable String id,
            @RequestBody Grade gradeDetails,
            @AuthenticationPrincipal Jwt jwt) {
        String teacherId = jwt.getSubject();
        Grade updatedGrade = gradeService.updateGrade(id, gradeDetails, teacherId);
        return ResponseEntity.ok(updatedGrade);
    }

    @GetMapping("/class/{classId}")
    @PreAuthorize("hasRole('teacher')")
    public List<Grade> getClassGrades(
            @PathVariable String classId,
            @AuthenticationPrincipal Jwt jwt) {
        String teacherId = jwt.getSubject();
        return gradeService.getGradesByClass(classId, teacherId);
    }

    // ================= STUDENT ENDPOINTS ================= //
    @GetMapping
    @PreAuthorize("hasRole('student')")
    public List<Grade> getStudentGrades(
            @AuthenticationPrincipal Jwt jwt) {
        String studentId = jwt.getSubject();
        return gradeService.getGradesByStudent(studentId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('student')")
    public ResponseEntity<Grade> getGrade(
            @PathVariable String id,
            @AuthenticationPrincipal Jwt jwt) {
        String studentId = jwt.getSubject();
        Grade grade = gradeService.getGradeById(id, studentId);
        return ResponseEntity.ok(grade);
    }

    // ================= ADMIN ENDPOINTS ================= //
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<Grade>> getAllGrades() {
        List<Grade> grades = gradeService.getAllGrades();
        return ResponseEntity.ok(grades);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Void> deleteGrade(@PathVariable String id) {
        gradeService.deleteGrade(id);
        return ResponseEntity.noContent().build();
    }
}