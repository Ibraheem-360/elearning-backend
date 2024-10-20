package com.ncnmo.aspire.elearning.repository;

import com.ncnmo.aspire.elearning.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    @Query("SELECT COUNT(l) FROM Like l WHERE l.discussion.id = :discussionId")
    int countByDiscussionId(Long discussionId);

    @Query("SELECT COUNT(l) FROM Like l WHERE l.comment.id = :commentId")
    int countByCommentId(Long commentId);

    @Query("SELECT l FROM Like l WHERE l.user.id = :userId AND (l.discussion.id = :discussionId OR l.comment.id = :commentId)")
    Optional<Like> findByUserAndDiscussionOrComment(@Param("userId") Long userId,
                                                    @Param("discussionId") Long discussionId,
                                                    @Param("commentId") Long commentId);
}
