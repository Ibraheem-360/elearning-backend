package com.ncnmo.aspire.elearning.service;

import com.ncnmo.aspire.elearning.dto.CommentDTO;
import com.ncnmo.aspire.elearning.model.Comment;
import com.ncnmo.aspire.elearning.model.Discussion;
import com.ncnmo.aspire.elearning.model.User;
import com.ncnmo.aspire.elearning.repository.CommentRepository;
import com.ncnmo.aspire.elearning.repository.DiscussionRepository;
import com.ncnmo.aspire.elearning.repository.LikeRepository;
import com.ncnmo.aspire.elearning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private DiscussionRepository discussionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;

    public CommentDTO addComment(CommentDTO commentDTO) {
        Discussion discussion = discussionRepository.findById(commentDTO.getDiscussionId())
                .orElseThrow(() -> new RuntimeException("Discussion not found"));

        User user = userRepository.findById(commentDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setDiscussion(discussion);
        comment.setUser(user);

        // Set the creation date
        comment.setCreatedDate(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);
        return convertToDTO(savedComment);
    }

    public CommentDTO updateComment(Long id, CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        comment.setContent(commentDTO.getContent());
        comment.setModifiedDate(LocalDateTime.now());  // Set the updated date

        Comment updatedComment = commentRepository.save(comment);
        return convertToDTO(updatedComment);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> getCommentsByDiscussion(Long discussionId) {
        return commentRepository.findByDiscussionId(discussionId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    CommentDTO convertToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setDiscussionId(comment.getDiscussion().getId());
        dto.setUserId(comment.getUser().getId());
        dto.setUsername(comment.getUser().getUsername());

        // Calculate and set like count for the comment
        int likeCount = likeRepository.countByCommentId(comment.getId());
        dto.setLikeCount(likeCount);

        // Set other fields
        dto.setCreatedAt(comment.getCreatedDate());
        dto.setUpdatedAt(comment.getModifiedDate());

        return dto;
    }

    private Comment convertToEntity(CommentDTO dto) {
        Comment comment = new Comment();
        comment.setId(dto.getId());
        comment.setContent(dto.getContent());

        // Fetch discussion and user
        Discussion discussion = discussionRepository.findById(dto.getDiscussionId())
                .orElseThrow(() -> new RuntimeException("Discussion not found"));
        comment.setDiscussion(discussion);

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        comment.setUser(user);

        return comment;
    }
}
