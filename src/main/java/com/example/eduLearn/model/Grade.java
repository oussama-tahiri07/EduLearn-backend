package com.example.eduLearn.model;

import java.time.LocalDateTime;

public class Grade {
	private String id;           // UUID
    private String subjectCode;  // e.g., "MATH101"
    private String subjectName;  // e.g., "Calculus I"
    private Double score;        // 0.0 - 100.0
    private String studentId;    // Supabase user_id of student (from JWT)
    private String teacherId;    // Supabase user_id of teacher (auto-filled)
    private String classId;      // Links to the class
    private String feedback;
    private LocalDateTime gradedAt;
    
    public Grade() {
        this.gradedAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }
    public String getUserId() { return studentId; }
    public void setUserId(String studentId) { this.studentId = studentId; }
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
	public String getStudentId() { return studentId; }
	public void setStudentId(String studentId) { this.studentId = studentId; }
	public String getTeacherId() { return teacherId; }
	public void setTeacherId(String teacherId) { this.teacherId = teacherId; }
	public String getClassId() { return classId; }
	public void setClassId(String classId) { this.classId = classId; }
	public LocalDateTime getGradedAt() { return gradedAt; }
	public void setGradedAt(LocalDateTime gradedAt) { this.gradedAt = gradedAt; }
    
}