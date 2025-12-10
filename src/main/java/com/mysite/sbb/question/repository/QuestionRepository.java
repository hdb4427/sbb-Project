package com.mysite.sbb.question.repository;

import com.mysite.sbb.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    // JPA에서도 메서드 이름 규칙이 같아서 그대로 사용 가능
    Page<Question> findBySubjectContainingIgnoreCaseOrContentContainingIgnoreCase(
            String subject,
            String content,
            Pageable pageable
    );
}