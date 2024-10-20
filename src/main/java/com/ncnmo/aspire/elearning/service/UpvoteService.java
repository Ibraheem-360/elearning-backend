package com.ncnmo.aspire.elearning.service;

import com.ncnmo.aspire.elearning.dto.UpvoteDTO;
import com.ncnmo.aspire.elearning.model.Comment;
import com.ncnmo.aspire.elearning.model.Discussion;
import com.ncnmo.aspire.elearning.model.Upvote;
import com.ncnmo.aspire.elearning.model.User;
import com.ncnmo.aspire.elearning.repository.CommentRepository;
import com.ncnmo.aspire.elearning.repository.DiscussionRepository;
import com.ncnmo.aspire.elearning.repository.UpvoteRepository;
import com.ncnmo.aspire.elearning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpvoteService {

    @Autowired
    private UpvoteRepository upvoteRepository;

    @Autowired
    private DiscussionRepository discussionRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public UpvoteDTO upvoteDiscussionOrComment(UpvoteDTO upvoteDTO) {
        User user = userRepository.findById(upvoteDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validation: Ensure that either discussionId or commentId is provided, but not both
        if ((upvoteDTO.getDiscussionId() == null && upvoteDTO.getCommentId() == null) ||
                (upvoteDTO.getDiscussionId() != null && upvoteDTO.getCommentId() != null)) {
            throw new IllegalArgumentException("You must provide either a discussionId or a commentId, but not both.");
        }

        Upvote upvote = new Upvote();
        upvote.setUser(user);

        // Handle discussion upvote
        if (upvoteDTO.getDiscussionId() != null) {
            Discussion discussion = discussionRepository.findById(upvoteDTO.getDiscussionId())
                    .orElseThrow(() -> new RuntimeException("Discussion not found"));
            upvote.setDiscussion(discussion);

            // Mark the discussion as answered
            discussion.setAnswered(true);
            discussionRepository.save(discussion);  // Save the discussion with the updated state
        }

        // Handle comment upvote
        if (upvoteDTO.getCommentId() != null) {
            Comment comment = commentRepository.findById(upvoteDTO.getCommentId())
                    .orElseThrow(() -> new RuntimeException("Comment not found"));
            upvote.setComment(comment);

            // Mark the associated discussion as answered
            Discussion discussion = comment.getDiscussion();
            discussion.setAnswered(true);
            discussionRepository.save(discussion);  // Save the discussion with the updated state
        }

        Upvote savedUpvote = upvoteRepository.save(upvote);
        return convertToDTO(savedUpvote);
    }


    private UpvoteDTO convertToDTO(Upvote upvote) {
        UpvoteDTO dto = new UpvoteDTO();
        dto.setId(upvote.getId());
        dto.setUserId(upvote.getUser().getId());

        if (upvote.getDiscussion() != null) {
            dto.setDiscussionId(upvote.getDiscussion().getId());
        }

        if (upvote.getComment() != null) {
            dto.setCommentId(upvote.getComment().getId());
        }

        return dto;
    }

    private Upvote convertToEntity(UpvoteDTO dto) {
        Upvote upvote = new Upvote();

        // Fetch user and discussion
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        upvote.setUser(user);

        Discussion discussion = discussionRepository.findById(dto.getDiscussionId())
                .orElseThrow(() -> new RuntimeException("Discussion not found"));
        upvote.setDiscussion(discussion);

        return upvote;
    }
}
