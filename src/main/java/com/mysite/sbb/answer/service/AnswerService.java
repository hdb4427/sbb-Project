package com.mysite.sbb.answer.service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.answer.dto.AnswerDto;
import com.mysite.sbb.answer.entity.Answer;
import com.mysite.sbb.answer.repository.AnswerRespository;
import com.mysite.sbb.member.entity.Member;
import com.mysite.sbb.question.entity.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRespository answerRespository;

    public Answer create(Question question, AnswerDto dto, Member author) {

        Answer answer = Answer.builder()
                .content(dto.getContent())
                .author(author)       // Member 객체
                .question(question)   // Question 객체
                .createDate(LocalDateTime.now())
                .build();

        return answerRespository.save(answer);
    }

    // ID 타입 String -> Integer
    public Answer getAnswer(Integer id) {
        return answerRespository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("answer not found"));
    }

    public void modify(Answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        answerRespository.save(answer);
    }

    public void delete(Answer answer) {
        answerRespository.delete(answer);
    }
}