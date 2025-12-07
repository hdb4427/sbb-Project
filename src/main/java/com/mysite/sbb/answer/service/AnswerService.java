package com.mysite.sbb.answer.service;

import com.mysite.sbb.answer.dto.AnswerDto;
import com.mysite.sbb.answer.entity.Answer;
import com.mysite.sbb.answer.repository.AnswerRespository;
import com.mysite.sbb.member.entity.Member;
import com.mysite.sbb.question.entity.Question;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AnswerService {

  private final AnswerRespository answerRespository;

  public void create(Question question, AnswerDto answerDto, Member member) {
    Answer answer = Answer.builder()
        .content(answerDto.getContent())
        .question(question)
        .author(member) // 답변 작성자 설정
        .build();

    answerRespository.save(answer);
  }

  public Answer getAnswer(Long id) {
    Answer answer = answerRespository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("답변을 찾을 수 없습니다: " + id));
    return answer;
  }

  public void modify(Answer answer, String content) {
    answer.setContent(content);
    answerRespository.save(answer);
  }

  public void delete(Answer answer) {
    answerRespository.delete(answer);
  }
}
