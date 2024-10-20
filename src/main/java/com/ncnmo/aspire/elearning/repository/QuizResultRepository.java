package com.ncnmo.aspire.elearning.repository;

import com.ncnmo.aspire.elearning.model.Question;
import com.ncnmo.aspire.elearning.model.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {

}
