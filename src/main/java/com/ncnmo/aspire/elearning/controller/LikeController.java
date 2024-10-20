package com.ncnmo.aspire.elearning.controller;

import com.ncnmo.aspire.elearning.dto.LikeDTO;
import com.ncnmo.aspire.elearning.model.User;
import com.ncnmo.aspire.elearning.service.EnrollmentService;
import com.ncnmo.aspire.elearning.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private EnrollmentService enrollmentService;

    // Like a discussion or a comment
    @PostMapping
    public ResponseEntity<?> like(@RequestBody LikeDTO likeDTO) {
        User currentUser = enrollmentService.getCurrentUser();

        // Set the userId in the DTO
        likeDTO.setUserId(currentUser.getId());

        try {
            LikeDTO createdLike = likeService.likeDiscussionOrComment(likeDTO);
            return new ResponseEntity<>(createdLike, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Unlike a discussion or a comment
    @DeleteMapping
    public ResponseEntity<?> unlike(@RequestBody LikeDTO likeDTO) {
        User currentUser = enrollmentService.getCurrentUser();

        // Set the userId in the DTO
        likeDTO.setUserId(currentUser.getId());

        try {
            likeService.unlikeDiscussionOrComment(likeDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
