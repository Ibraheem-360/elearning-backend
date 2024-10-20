package com.ncnmo.aspire.elearning.repository;

import com.ncnmo.aspire.elearning.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Find all comments by discussion ID
    List<Comment> findByDiscussionId(Long discussionId);
}
