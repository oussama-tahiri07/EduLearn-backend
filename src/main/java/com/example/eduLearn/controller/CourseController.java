package com.example.eduLearn.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import com.example.eduLearn.model.Course;
import com.example.eduLearn.service.SupabaseCourseService;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final SupabaseCourseService CourseService;

    public CourseController(SupabaseCourseService classService) {
        this.CourseService = classService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<Course> createCourse(
            @RequestBody Course classRequest,
            @AuthenticationPrincipal Jwt jwt) {
        String teacherId = jwt.getSubject();
        Course newCourse = CourseService.createCourse(classRequest, teacherId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCourse);
    }

    @GetMapping("/my-classes")
    @PreAuthorize("hasRole('teacher')")
    public List<Course> getTeacherCourse(@AuthenticationPrincipal Jwt jwt) {
        String teacherId = jwt.getSubject();
        return CourseService.getCourseByTeacher(teacherId);
    }
}