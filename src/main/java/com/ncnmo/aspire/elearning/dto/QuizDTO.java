package com.ncnmo.aspire.elearning.dto;

import lombok.Data;
import java.util.List;

@Data
public class QuizDTO {
    private Long id;
    private String title;
    private Long courseId;  // Store the course ID to associate the quiz with a course
    private List<QuestionDTO> questions;
}
