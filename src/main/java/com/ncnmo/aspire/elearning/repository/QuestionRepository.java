package com.ncnmo.aspire.elearning.repository;

import com.ncnmo.aspire.elearning.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

}
