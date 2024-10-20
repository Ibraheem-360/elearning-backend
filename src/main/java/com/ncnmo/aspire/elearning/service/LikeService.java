package com.ncnmo.aspire.elearning.service;

import com.ncnmo.aspire.elearning.dto.LikeDTO;
import com.ncnmo.aspire.elearning.model.Like;
import com.ncnmo.aspire.elearning.model.User;
import com.ncnmo.aspire.elearning.model.Discussion;
import com.ncnmo.aspire.elearning.model.Comment;
import com.ncnmo.aspire.elearning.repository.LikeRepository;
import com.ncnmo.aspire.elearning.repository.UserRepository;
import com.ncnmo.aspire.elearning.repository.DiscussionRepository;
import com.ncnmo.aspire.elearning.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DiscussionRepository discussionRepository;

    @Autowired
    private CommentRepository commentRepository;

    public LikeDTO likeDiscussionOrComment(LikeDTO likeDTO) {
        User user = userRepository.findById(likeDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if the like already exists
        Optional<Like> existingLike = likeRepository.findByUserAndDiscussionOrComment(user.getId(), likeDTO.getDiscussionId(), likeDTO.getCommentId());
        if (existingLike.isPresent()) {
            throw new RuntimeException("User has already liked this discussion or comment");
        }

        Like like = new Like();
        like.setUser(user);

        if (likeDTO.getDiscussionId() != null) {
            Discussion discussion = discussionRepository.findById(likeDTO.getDiscussionId())
                    .orElseThrow(() -> new RuntimeException("Discussion not found"));
            like.setDiscussion(discussion);
        }

        if (likeDTO.getCommentId() != null) {
            Comment comment = commentRepository.findById(likeDTO.getCommentId())
                    .orElseThrow(() -> new RuntimeException("Comment not found"));
            like.setComment(comment);
        }

        Like savedLike = likeRepository.save(like);
        return convertToDTO(savedLike);
    }

    public void unlikeDiscussionOrComment(LikeDTO likeDTO) {
        User user = userRepository.findById(likeDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Like> existingLike = likeRepository.findByUserAndDiscussionOrComment(user.getId(), likeDTO.getDiscussionId(), likeDTO.getCommentId());

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
        } else {
            throw new RuntimeException("Like not found");
        }
    }

    private LikeDTO convertToDTO(Like like) {
        LikeDTO dto = new LikeDTO();
        dto.setId(like.getId());
        dto.setUserId(like.getUser().getId());
        if (like.getDiscussion() != null) {
            dto.setDiscussionId(like.getDiscussion().getId());
        }
        if (like.getComment() != null) {
            dto.setCommentId(like.getComment().getId());
        }
        return dto;
    }

    private Like convertToEntity(LikeDTO dto) {
        Like like = new Like();

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        like.setUser(user);

        if (dto.getDiscussionId() != null) {
            Discussion discussion = discussionRepository.findById(dto.getDiscussionId())
                    .orElseThrow(() -> new RuntimeException("Discussion not found"));
            like.setDiscussion(discussion);
        }

        if (dto.getCommentId() != null) {
            Comment comment = commentRepository.findById(dto.getCommentId())
                    .orElseThrow(() -> new RuntimeException("Comment not found"));
            like.setComment(comment);
        }

        return like;
    }
}


