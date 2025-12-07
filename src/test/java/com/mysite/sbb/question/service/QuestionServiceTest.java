package com.mysite.sbb.question.service;

import com.mysite.sbb.question.dto.QuestionDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QuestionServiceTest {

  @Autowired
  private QuestionService questionService;

  //@Transactional
  @Test
  void createDumyData() {
    for (int i = 1; i < 301 ; i++) {
      QuestionDto questionDto = QuestionDto.builder()
          .subject("테스트 질문 " + i)
          .content("테스트 내용 " + i)
          .build();
//      questionService.create(questionDto);
    }
//    assertEquals(306, questionService.getList().size(), "질문 데이터가 300개가 되어야 합니다.");
  }
}