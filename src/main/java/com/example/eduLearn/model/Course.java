package com.example.eduLearn.model;

import java.time.LocalDateTime;

public class Course {
    private String id;
    private String name;
    private String subjectCode;
    private String teacherId;
    private LocalDateTime createdAt;

    // Constructors
    public Course() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
    public String getTeacherId() { return teacherId; }
    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}