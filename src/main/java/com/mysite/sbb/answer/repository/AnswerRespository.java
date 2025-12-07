package com.mysite.sbb.answer.repository;

import com.mysite.sbb.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRespository extends JpaRepository<Answer, Long> {
}
