package com.example.eduLearn.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.eduLearn.model.Course;
import com.example.eduLearn.model.Homework;
import com.example.eduLearn.service.SupabaseCourseService;
import com.example.eduLearn.service.SupabaseHomeworkService;

@RestController
@RequestMapping("/api/teacher")
@PreAuthorize("hasRole('TEACHER')")
public class TeacherController {

    private final SupabaseCourseService classService = new SupabaseCourseService();
    private final SupabaseHomeworkService homeworkService = new SupabaseHomeworkService();

    // Create a class
    @PostMapping("/classes")
    public Course createClass(
            @RequestBody Course classRequest,
            @AuthenticationPrincipal Jwt jwt) {
        String teacherId = jwt.getSubject();
        return classService.createCourse(classRequest, teacherId);
    }

    // Assign homework
    @PostMapping("/homeworks")
    public Homework createHomework(
            @RequestBody Homework homework,
            @AuthenticationPrincipal Jwt jwt) {
        String teacherId = jwt.getSubject();
        return homeworkService.createHomework(homework, teacherId);
    }
}