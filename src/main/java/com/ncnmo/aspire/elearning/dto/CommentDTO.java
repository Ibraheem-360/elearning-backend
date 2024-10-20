package com.ncnmo.aspire.elearning.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long id;
    private String content;
    private Long discussionId;  // ID of the associated discussion
    private Long userId;        // ID of the user who posted the comment
    private String username;    // Name of the user (optional)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int likeCount; // Total likes for this comment
}
