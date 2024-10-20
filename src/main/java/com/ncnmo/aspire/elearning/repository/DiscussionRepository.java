package com.ncnmo.aspire.elearning.repository;

import com.ncnmo.aspire.elearning.model.Discussion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscussionRepository extends JpaRepository<Discussion, Long> {

    // Find discussions by course ID (to filter discussions by course)
    List<Discussion> findByCourseId(Long courseId);

    // Custom search query to find discussions by keyword in the question
    @Query("SELECT d FROM Discussion d WHERE LOWER(d.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Discussion> searchDiscussionsByKeyword(@Param("keyword") String keyword);
}
