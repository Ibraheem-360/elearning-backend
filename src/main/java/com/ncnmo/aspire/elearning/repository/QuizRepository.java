package com.ncnmo.aspire.elearning.repository;

import com.ncnmo.aspire.elearning.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    // Custom query to find all quizzes by course ID
    List<Quiz> findByCourseId(Long courseId);
}
