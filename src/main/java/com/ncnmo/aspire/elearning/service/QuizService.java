package com.ncnmo.aspire.elearning.service;

import com.ncnmo.aspire.elearning.dto.QuizDTO;
import com.ncnmo.aspire.elearning.dto.QuestionDTO;
import com.ncnmo.aspire.elearning.model.Course;
import com.ncnmo.aspire.elearning.model.Quiz;
import com.ncnmo.aspire.elearning.model.Question;
import com.ncnmo.aspire.elearning.repository.CourseRepository;
import com.ncnmo.aspire.elearning.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private CourseRepository courseRepository;

    public QuizDTO saveQuiz(QuizDTO quizDTO) {
        // Log to check the input DTO before saving
        System.out.println("QuizDTO: " + quizDTO);
        quizDTO.getQuestions().forEach(q -> System.out.println("QuestionDTO: " + q));

        Quiz quiz = convertToEntity(quizDTO);
        Quiz savedQuiz = quizRepository.save(quiz);
        return convertToDTO(savedQuiz);
    }



    public QuizDTO updateQuiz(Long id, QuizDTO quizDTO) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        quiz.setTitle(quizDTO.getTitle());
        quiz.setQuestions(quizDTO.getQuestions().stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList()));
        Quiz updatedQuiz = quizRepository.save(quiz);
        return convertToDTO(updatedQuiz);
    }

    public QuizDTO findById(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        return convertToDTO(quiz);
    }

    public List<QuizDTO> findQuizzesByCourseId(Long courseId) {
        return quizRepository.findByCourseId(courseId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }

    // Conversion methods
    QuizDTO convertToDTO(Quiz quiz) {
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setId(quiz.getId());
        quizDTO.setTitle(quiz.getTitle());
        quizDTO.setCourseId(quiz.getCourse().getId());

        List<QuestionDTO> questionDTOs = quiz.getQuestions().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        quizDTO.setQuestions(questionDTOs);

        return quizDTO;
    }

    Quiz convertToEntity(QuizDTO quizDTO) {
        Quiz quiz = new Quiz();
        quiz.setId(quizDTO.getId());  // This will be null for new quizzes
        quiz.setTitle(quizDTO.getTitle());

        // Ensure that the course is correctly linked by finding it using courseId
        Course course = courseRepository.findById(quizDTO.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        quiz.setCourse(course);

        List<Question> questions = quizDTO.getQuestions().stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());

        // Set the quiz reference for each question
        questions.forEach(question -> question.setQuiz(quiz));

        quiz.setQuestions(questions);

        return quiz;
    }



    QuestionDTO convertToDTO(Question question) {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(question.getId());
        questionDTO.setQuestionText(question.getQuestionText());
        questionDTO.setOptionA(question.getOptionA());
        questionDTO.setOptionB(question.getOptionB());
        questionDTO.setOptionC(question.getOptionC());
        questionDTO.setOptionD(question.getOptionD());
        questionDTO.setCorrectAnswer(question.getCorrectAnswer());

        return questionDTO;
    }

    Question convertToEntity(QuestionDTO questionDTO) {
        Question question = new Question();

        // Ensure the id is not set when creating a new question
        if (questionDTO.getId() != null) {
            question.setId(questionDTO.getId());
        }

        question.setQuestionText(questionDTO.getQuestionText());
        question.setOptionA(questionDTO.getOptionA());
        question.setOptionB(questionDTO.getOptionB());
        question.setOptionC(questionDTO.getOptionC());
        question.setOptionD(questionDTO.getOptionD());
        question.setCorrectAnswer(questionDTO.getCorrectAnswer());

        // Note: The quiz reference will be set in the Quiz conversion

        return question;
    }


}
