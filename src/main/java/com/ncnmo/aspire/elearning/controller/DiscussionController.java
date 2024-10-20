package com.ncnmo.aspire.elearning.controller;

import com.ncnmo.aspire.elearning.dto.DiscussionDTO;
import com.ncnmo.aspire.elearning.model.User;
import com.ncnmo.aspire.elearning.security.JwtUtil;
import com.ncnmo.aspire.elearning.service.DiscussionService;
import com.ncnmo.aspire.elearning.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discussions")
public class DiscussionController {

    @Autowired
    private DiscussionService discussionService;

    @Autowired
    private EnrollmentService enrollmentService;

    // Create a new discussion
    @PostMapping("/discussions")
    public ResponseEntity<?> createDiscussion(@RequestBody DiscussionDTO discussionDTO) {
        // Get the current user
        User currentUser = enrollmentService.getCurrentUser();

        // Set the userId in the DTO before passing it to the service
        discussionDTO.setUserId(currentUser.getId());

        try {
            DiscussionDTO createdDiscussion = discussionService.createDiscussion(discussionDTO);
            return new ResponseEntity<>(createdDiscussion, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Get discussions by course ID
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<DiscussionDTO>> getDiscussionsByCourse(@PathVariable Long courseId) {
        List<DiscussionDTO> discussions = discussionService.getDiscussionsByCourse(courseId);
        return new ResponseEntity<>(discussions, HttpStatus.OK);
    }

    // Get a specific discussion by its ID
    @GetMapping("/{id}")
    public ResponseEntity<DiscussionDTO> getDiscussionById(@PathVariable Long id) {
        DiscussionDTO discussion = discussionService.getDiscussionById(id);
        return new ResponseEntity<>(discussion, HttpStatus.OK);
    }


    @GetMapping("/search")
    public ResponseEntity<List<DiscussionDTO>> searchDiscussions(@RequestParam String keyword) {
        List<DiscussionDTO> discussions = discussionService.searchDiscussions(keyword);
        return new ResponseEntity<>(discussions, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDiscussion(@PathVariable Long id, @RequestBody DiscussionDTO discussionDTO) {
        try {
            DiscussionDTO updatedDiscussion = discussionService.updateDiscussion(id, discussionDTO);
            return new ResponseEntity<>(updatedDiscussion, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDiscussion(@PathVariable Long id) {
        try {
            discussionService.deleteDiscussion(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

