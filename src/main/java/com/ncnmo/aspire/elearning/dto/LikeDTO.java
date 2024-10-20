package com.ncnmo.aspire.elearning.dto;

import lombok.Data;

@Data
public class LikeDTO {
    private Long id;
    private Long userId;         // ID of the user who liked
    private Long discussionId;   // Optional: ID of the discussion being liked
    private Long commentId;      // Optional: ID of the comment being liked
}
