package com.example.eduLearn.controller;

import com.example.eduLearn.model.Homework;
import com.example.eduLearn.service.SupabaseHomeworkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/homeworks")
public class HomeworkController {

    private final SupabaseHomeworkService homeworkService;

    public HomeworkController(SupabaseHomeworkService homeworkService) {
        this.homeworkService = homeworkService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<Homework> createHomework(
            @RequestBody Homework homework,
            @AuthenticationPrincipal Jwt jwt) {
        Homework newHomework = homeworkService.createHomework(homework, jwt.getSubject());
        return ResponseEntity.status(HttpStatus.CREATED).body(newHomework);
    }

    @GetMapping("/courses/{classId}")
    public List<Homework> getCourseHomeworks(@PathVariable String classId) {
        return homeworkService.getHomeworksByCourse(classId);
    }
}