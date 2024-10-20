package com.ncnmo.aspire.elearning.service;

import com.ncnmo.aspire.elearning.dto.EnrollmentDTO;
import com.ncnmo.aspire.elearning.model.Course;
import com.ncnmo.aspire.elearning.model.Enrollment;
import com.ncnmo.aspire.elearning.model.User;
import com.ncnmo.aspire.elearning.repository.EnrollmentRepository;
import com.ncnmo.aspire.elearning.repository.CourseRepository;
import com.ncnmo.aspire.elearning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonCompletionService lessonCompletionService;  // Inject LessonCompletionService for completion check

    public EnrollmentDTO saveEnrollment(Long courseId) {
        // Get the currently authenticated user
        User user = getCurrentUser();

        // Find the course by its ID
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Check if the user is already enrolled in the course
        if (isUserEnrolledInCourse(user.getId(), courseId)) {
            throw new RuntimeException("User is already enrolled in this course");
        }

        // Create a new enrollment
        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setCompleted(false); // Set initial enrollment as not completed

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return convertToDTOWithCompletionStatus(savedEnrollment);
    }

    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional(readOnly = true)
    public List<EnrollmentDTO> findEnrollmentsByUserId(Long userId) {
        return enrollmentRepository.findByUserId(userId).stream()
                .map(this::convertToDTOWithCompletionStatus)  // Updated conversion method
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EnrollmentDTO> findAllEnrollments() {
        return enrollmentRepository.findAll().stream()
                .map(this::convertToDTOWithCompletionStatus)  // Updated conversion method
                .collect(Collectors.toList());
    }

    public void deleteEnrollment(Long id) {
        enrollmentRepository.deleteById(id);
    }

    public boolean isUserEnrolledInCourse(Long userId, Long courseId) {
        return enrollmentRepository.existsByUserIdAndCourseId(userId, courseId);
    }

    public boolean isCurrentUserEnrolledInCourse(Long courseId) {
        User user = getCurrentUser();
        return isUserEnrolledInCourse(user.getId(), courseId);
    }

    @Transactional(readOnly = true)
    public List<EnrollmentDTO> findEnrollmentsByCourseId(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId).stream()
                .map(this::convertToDTOWithCompletionStatus)
                .collect(Collectors.toList());
    }

    // Convert Enrollment entity to DTO with dynamic completion status
    private EnrollmentDTO convertToDTOWithCompletionStatus(Enrollment enrollment) {
        EnrollmentDTO enrollmentDTO = new EnrollmentDTO();
        enrollmentDTO.setId(enrollment.getId());
        enrollmentDTO.setUserId(enrollment.getUser().getId());
        enrollmentDTO.setCourseId(enrollment.getCourse().getId());

        // Dynamically check if the course is completed by the user
        boolean isCourseCompleted = lessonCompletionService.isCourseCompletedByUser(
                enrollment.getUser().getId(), enrollment.getCourse().getId());
        enrollmentDTO.setCompleted(isCourseCompleted);  // Dynamically set the completed status

        return enrollmentDTO;
    }

    // Convert DTO to Enrollment entity (for future needs, e.g., updates)
    private Enrollment convertToEntity(EnrollmentDTO enrollmentDTO) {
        Enrollment enrollment = new Enrollment();
        enrollment.setId(enrollmentDTO.getId());
        User user = userRepository.findById(enrollmentDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(enrollmentDTO.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setCompleted(enrollmentDTO.isCompleted());
        return enrollment;
    }
}
