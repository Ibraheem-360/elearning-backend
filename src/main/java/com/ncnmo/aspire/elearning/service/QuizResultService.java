package com.ncnmo.aspire.elearning.service;

import com.ncnmo.aspire.elearning.dto.QuizSubmissionDTO;
import com.ncnmo.aspire.elearning.model.Question;
import com.ncnmo.aspire.elearning.model.QuizResult;
import com.ncnmo.aspire.elearning.repository.QuestionRepository;
import com.ncnmo.aspire.elearning.repository.QuizRepository;
import com.ncnmo.aspire.elearning.repository.QuizResultRepository;
import com.ncnmo.aspire.elearning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class QuizResultService {
    @Autowired
    private QuizResultRepository quizResultRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public QuizResult saveQuizResult(QuizSubmissionDTO quizSubmissionDTO) {
        int score = 0;
        for (Map.Entry<Long, String> entry : quizSubmissionDTO.getAnswers().entrySet()) {
            Long questionId = entry.getKey();
            String selectedAnswer = entry.getValue();
            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new RuntimeException("Question not found"));
            if (question.getCorrectAnswer().equals(selectedAnswer)) {
                score++;
            }
        }

        QuizResult quizResult = new QuizResult();
        quizResult.setUser(userRepository.findById(quizSubmissionDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found")));
        quizResult.setQuiz(quizRepository.findById(quizSubmissionDTO.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz not found")));
        quizResult.setScore(score);

        return quizResultRepository.save(quizResult);
    }
}
