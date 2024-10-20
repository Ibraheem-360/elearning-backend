package com.ncnmo.aspire.elearning.controller;

import com.ncnmo.aspire.elearning.dto.UpvoteDTO;
import com.ncnmo.aspire.elearning.model.User;
import com.ncnmo.aspire.elearning.service.EnrollmentService;
import com.ncnmo.aspire.elearning.service.UpvoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/upvotes")
public class UpvoteController {

    @Autowired
    private UpvoteService upvoteService;

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/upvote")
    public ResponseEntity<UpvoteDTO> upvoteDiscussionOrComment(@RequestBody UpvoteDTO upvoteDTO) {
        // Get the current user
        User currentUser = enrollmentService.getCurrentUser();
        upvoteDTO.setUserId(currentUser.getId());

        UpvoteDTO createdUpvote = upvoteService.upvoteDiscussionOrComment(upvoteDTO);
        return new ResponseEntity<>(createdUpvote, HttpStatus.CREATED);
    }
}
