package com.ncnmo.aspire.elearning.controller;

import com.ncnmo.aspire.elearning.dto.CommentDTO;
import com.ncnmo.aspire.elearning.model.User;
import com.ncnmo.aspire.elearning.service.CommentService;
import com.ncnmo.aspire.elearning.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private EnrollmentService enrollmentService;

    // Add a comment to a discussion
    @PostMapping
    public ResponseEntity<?> addComment(@RequestBody CommentDTO commentDTO) {
        User currentUser = enrollmentService.getCurrentUser();

        // Set the userId in the DTO
        commentDTO.setUserId(currentUser.getId());

        try {
            CommentDTO createdComment = commentService.addComment(commentDTO);
            return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update a comment
    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long id, @RequestBody CommentDTO commentDTO) {
        try {
            CommentDTO updatedComment = commentService.updateComment(id, commentDTO);
            return new ResponseEntity<>(updatedComment, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete a comment
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        try {
            commentService.deleteComment(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all comments for a specific discussion
    @GetMapping("/discussion/{discussionId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByDiscussion(@PathVariable Long discussionId) {
        List<CommentDTO> comments = commentService.getCommentsByDiscussion(discussionId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}
