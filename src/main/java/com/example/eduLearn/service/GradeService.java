package com.example.eduLearn.service;

import com.example.eduLearn.model.Grade;
import java.util.List;

public interface GradeService {
    Grade createGrade(Grade grade, String userId);
    List<Grade> getGradesByUser(String userId);
    Grade getGradeById(String id, String userId);
    Grade updateGrade(String id, Grade gradeDetails, String userId);
    void deleteGrade(String id, String userId);
    List<Grade> getAllGrades(); // For admin/teacher access
    List<Grade> getGradesBySubject(String subjectCode, String userId);
}