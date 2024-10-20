package com.ncnmo.aspire.elearning.model;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
public class Upvote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "discussion_id", nullable = true)
    private Discussion discussion;  // Upvote is tied to a discussion

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = true)
    private Comment comment;  // Upvote is tied to a comment
}
