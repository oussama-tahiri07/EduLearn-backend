package com.example.eduLearn.model;

public class Grade {
    private String id;          // Matches Supabase's UUID or BigInt
    private String subjectCode; // e.g., "I12-S1"
    private String subjectName; // e.g., "CALCULUS I"
    private Double score;       // e.g., 18.0
    private String userId;      // Reference to Supabase user

    // Manual getters/setters (since Lombok isn't working)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}