package com.mysite.sbb.question.repository;

import com.mysite.sbb.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    Page<Question> findBySubjectContainingIgnoreCaseOrContentContainingIgnoreCase(
            String subject,
            String content,
            Pageable pageable
    );

    @Query("select q from Question q where q.category = :category " + "and (q.subject like %:kw% or q.content like %:kw%)")
    Page<Question> findAllByCategoryAndKeyword(@Param("category") String category, @Param("kw") String kw, Pageable pageable);

    List<Question> findTop10ByCategoryOrderByCreatedDesc(String category);
}