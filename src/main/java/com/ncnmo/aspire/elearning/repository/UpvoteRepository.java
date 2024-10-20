package com.ncnmo.aspire.elearning.repository;

import com.ncnmo.aspire.elearning.model.Upvote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpvoteRepository extends JpaRepository<Upvote, Long> {

    // Find an upvote for a specific discussion by a specific user (this ensures uniqueness)
    Upvote findByDiscussionIdAndUserId(Long discussionId, Long userId);

    // Find an upvote for a specific comment by a specific user (this ensures uniqueness)
    Upvote findByCommentIdAndUserId(Long commentId, Long userId);

    // Count total upvotes for a discussion (could be useful for statistics)
    long countByDiscussionId(Long discussionId);

    // Count total upvotes for a comment
    long countByCommentId(Long commentId);
}
