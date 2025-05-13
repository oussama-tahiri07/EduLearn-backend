package com.example.eduLearn.model;

import java.time.LocalDate;

public class Homework {
    private String id;
    private String classId;
    private String title;
    private String description;
    private LocalDate dueDate;
    private boolean published;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getClassId() { return classId; }
    public void setClassId(String classId) { this.classId = classId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public boolean isPublished() { return published; }
    public void setPublished(boolean published) { this.published = published; }
}