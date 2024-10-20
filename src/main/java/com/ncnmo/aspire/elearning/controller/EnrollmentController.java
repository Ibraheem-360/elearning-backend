package com.ncnmo.aspire.elearning.controller;

import com.ncnmo.aspire.elearning.dto.EnrollmentDTO;
import com.ncnmo.aspire.elearning.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/enroll")
    public ResponseEntity<String> createEnrollment(@RequestParam Long courseId) {
        try {
            EnrollmentDTO enrollment = enrollmentService.saveEnrollment(courseId);
            return new ResponseEntity<>("Enrollment successful for course with ID: " + enrollment.getCourseId(), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByUser(@PathVariable Long userId) {
        return new ResponseEntity<>(enrollmentService.findEnrollmentsByUserId(userId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByCourse(@PathVariable Long courseId) {
        return new ResponseEntity<>(enrollmentService.findEnrollmentsByCourseId(courseId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<EnrollmentDTO>> getAllEnrollments() {
        return new ResponseEntity<>(enrollmentService.findAllEnrollments(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/check-enrollment")
    public ResponseEntity<Boolean> checkEnrollment(@RequestParam Long courseId) {
        boolean isEnrolled = enrollmentService.isCurrentUserEnrolledInCourse(courseId);
        return new ResponseEntity<>(isEnrolled, HttpStatus.OK);
    }
}
