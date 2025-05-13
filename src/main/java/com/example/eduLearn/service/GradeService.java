package com.example.eduLearn.service;

import com.example.eduLearn.model.Grade;
import java.util.List;

public interface GradeService {
    Grade submitGrade(Grade grade, String teacherId);
    Grade getGradeById(String id, String userId);
    Grade updateGrade(String id, Grade gradeDetails, String userId);
    void deleteGradeByTeacher(String gradeId, String teacherId) throws Exception;
    void deleteGrade(String id);
    List<Grade> getAllGrades(); // For admin/teacher access
    List<Grade> getGradesBySubject(String subjectCode, String userId);
    List<Grade> getGradesByStudent(String studentId);
    List<Grade> getGradesByClass(String classId, String teacherId);
}