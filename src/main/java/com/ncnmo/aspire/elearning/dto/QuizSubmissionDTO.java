package com.ncnmo.aspire.elearning.dto;

import lombok.Data;

import java.util.Map;

@Data
public class QuizSubmissionDTO {
    private Long userId;
    private Long quizId;
    private Map<Long, String> answers;
}
