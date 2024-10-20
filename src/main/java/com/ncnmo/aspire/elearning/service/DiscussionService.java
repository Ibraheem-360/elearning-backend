package com.ncnmo.aspire.elearning.service;

import com.ncnmo.aspire.elearning.dto.DiscussionDTO;
import com.ncnmo.aspire.elearning.model.Course;
import com.ncnmo.aspire.elearning.model.Discussion;
import com.ncnmo.aspire.elearning.model.User;
import com.ncnmo.aspire.elearning.repository.CourseRepository;
import com.ncnmo.aspire.elearning.repository.DiscussionRepository;
import com.ncnmo.aspire.elearning.repository.LikeRepository;
import com.ncnmo.aspire.elearning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscussionService {

    @Autowired
    private DiscussionRepository discussionRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserRepository userRepository;

    public DiscussionDTO createDiscussion(DiscussionDTO discussionDTO) {
        Course course = courseRepository.findById(discussionDTO.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Get the currently logged-in user
        User currentUser = enrollmentService.getCurrentUser(); // Assuming this method retrieves the logged-in user

        Discussion discussion = new Discussion();
        discussion.setTitle(discussionDTO.getTitle());
        discussion.setContent(discussionDTO.getContent());
        discussion.setCourse(course);
        discussion.setUser(currentUser);  // Set the user who created the discussion

        // Automatically set the created date
        discussion.setCreatedDate(LocalDateTime.now());

        Discussion savedDiscussion = discussionRepository.save(discussion);
        return convertToDTO(savedDiscussion);
    }

    @Transactional(readOnly = true)
    public List<DiscussionDTO> getDiscussionsByCourse(Long courseId) {
        return discussionRepository.findByCourseId(courseId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DiscussionDTO getDiscussionById(Long id) {
        Discussion discussion = discussionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discussion not found"));
        return convertToDTO(discussion);
    }

    @Transactional(readOnly = true)
    public List<DiscussionDTO> searchDiscussions(String keyword) {
        List<Discussion> discussions = discussionRepository.searchDiscussionsByKeyword(keyword);
        return discussions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public DiscussionDTO updateDiscussion(Long id, DiscussionDTO discussionDTO) {
        // Find the existing discussion by ID
        Discussion discussion = discussionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discussion not found"));

        // Update the discussion fields
        discussion.setTitle(discussionDTO.getTitle());
        discussion.setContent(discussionDTO.getContent());
        discussion.setAnswered(discussionDTO.isAnswered());

        // Set the updated date to now
        discussion.setModifiedDate(LocalDateTime.now());

        // Save the updated discussion
        Discussion updatedDiscussion = discussionRepository.save(discussion);

        return convertToDTO(updatedDiscussion);
    }

    public void deleteDiscussion(Long id) {
        // Check if the discussion exists
        Discussion discussion = discussionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discussion not found"));

        // Delete the discussion
        discussionRepository.delete(discussion);
    }

    private DiscussionDTO convertToDTO(Discussion discussion) {
        DiscussionDTO dto = new DiscussionDTO();
        dto.setId(discussion.getId());
        dto.setTitle(discussion.getTitle());
        dto.setContent(discussion.getContent());
        dto.setCourseId(discussion.getCourse().getId());

        // Set user details
        if (discussion.getUser() != null) {
            dto.setUserId(discussion.getUser().getId());
            dto.setUsername(discussion.getUser().getUsername());
        }

        // Calculate and set like count for the discussion
        int likeCount = likeRepository.countByDiscussionId(discussion.getId());
        dto.setLikeCount(likeCount);

        // Set other fields
        dto.setAnswered(discussion.isAnswered());
        dto.setCreatedAt(discussion.getCreatedDate());
        dto.setUpdatedAt(discussion.getModifiedDate());

        // Ensure comments are not null
        dto.setComments(discussion.getComments() != null ?
                discussion.getComments().stream()
                        .map(commentService::convertToDTO)
                        .collect(Collectors.toList()) :
                new ArrayList<>());

        return dto;
    }



    private Discussion convertToEntity(DiscussionDTO dto) {
        Discussion discussion = new Discussion();
        discussion.setId(dto.getId());
        discussion.setTitle(dto.getTitle());
        discussion.setContent(dto.getContent());

        // Fetch course and set it to the discussion
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        discussion.setCourse(course);

        return discussion;
    }
}
