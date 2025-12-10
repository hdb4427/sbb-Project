package com.mysite.sbb.answer.service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.answer.dto.AnswerDto;
import com.mysite.sbb.answer.entity.Answer;
import com.mysite.sbb.answer.repository.AnswerRespository;
import com.mysite.sbb.member.entity.Member;
import com.mysite.sbb.question.entity.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// import java.time.LocalDateTime; // ❌ 날짜 관련 import 삭제 (자동 처리됨)

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRespository answerRespository;

    public Answer create(Question question, AnswerDto dto, Member author) {

        Answer answer = Answer.builder()
                .content(dto.getContent())
                .author(author)
                .question(question)
                // .createDate(LocalDateTime.now()) ❌ 삭제 (BaseEntity가 자동 처리)
                .build();

        return answerRespository.save(answer);
    }

    public Answer getAnswer(Integer id) {
        return answerRespository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("answer not found"));
    }

    public void modify(Answer answer, String content) {
        answer.setContent(content);
        // answer.setModifyDate(LocalDateTime.now()); ❌ 삭제 (BaseEntity가 자동 처리)
        answerRespository.save(answer);
    }

    public void delete(Answer answer) {
        answerRespository.delete(answer);
    }
}