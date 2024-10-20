package com.ncnmo.aspire.elearning.dto;

import lombok.Data;

@Data
public class UpvoteDTO {
    private Long id;
    private Long userId;
    private Long discussionId;   // ID of the discussion (optional if it's a discussion upvote)
    private Long commentId;      // ID of the comment (optional if it's a comment upvote)
}
