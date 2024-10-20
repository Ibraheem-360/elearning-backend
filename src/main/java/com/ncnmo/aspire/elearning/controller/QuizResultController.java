package com.ncnmo.aspire.elearning.controller;

import com.ncnmo.aspire.elearning.dto.QuizSubmissionDTO;
import com.ncnmo.aspire.elearning.model.QuizResult;
import com.ncnmo.aspire.elearning.service.QuizResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz-results")
public class QuizResultController {
    @Autowired
    private QuizResultService quizResultService;

    @PostMapping("/submit")
    public ResponseEntity<QuizResult> submitQuizResult(@RequestBody QuizSubmissionDTO quizSubmissionDTO) {
        return new ResponseEntity<>(quizResultService.saveQuizResult(quizSubmissionDTO), HttpStatus.CREATED);
    }
}
