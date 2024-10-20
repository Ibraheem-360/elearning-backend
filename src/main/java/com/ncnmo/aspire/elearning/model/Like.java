package com.ncnmo.aspire.elearning.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "_like")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "discussion_id", nullable = true)
    private Discussion discussion;   // A like can be attached to a discussion (optional)

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = true)
    private Comment comment;         // A like can be attached to a comment (optional)
}
