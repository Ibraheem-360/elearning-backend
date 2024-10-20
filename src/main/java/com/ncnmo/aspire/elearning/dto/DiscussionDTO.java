package com.ncnmo.aspire.elearning.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DiscussionDTO {
    private Long id;
    private String title;
    private String content;
    private Long courseId;  // ID of the associated course
    private Long userId;    // ID of the user who posted the discussion
    private String username; // Name of the user (optional, for easier display on frontend)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int likeCount; // Total likes for this discussion
    private boolean isAnswered; // Marks if the discussion has been marked as answered

    // List of comments associated with this discussion
    private List<CommentDTO> comments;
}
