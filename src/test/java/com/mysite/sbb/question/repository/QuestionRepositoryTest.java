package com.mysite.sbb.question.repository;

import com.mysite.sbb.question.entity.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuestionRepositoryTest {

  @Autowired
  private QuestionRepository questionRepository;

  @Test
  void testSave(){
    Question question = new Question();
    question.setSubject("sbb가 무엇인가요?");
    question.setContent("sbb에 대해서 알고 싶습니다.");
    System.out.println("question = " + question);

    Question savedQuestion = questionRepository.save(question);
    System.out.println("savedQuestion = " + savedQuestion);

//    Question question2 = new Question("test subject", "test content");
    Question question2 = Question.builder()
        .subject("test subject")
        .content("test content")
        .build();
    System.out.println("question2 = " + question2);
    Question savedQuestion2 = questionRepository.save(question2);
    System.out.println("savedQuestion2 = " + savedQuestion2);
    assertNotNull(savedQuestion2.getId());
    assertEquals("sbb가 무엇인가요?", savedQuestion2.getSubject());
  }

  @Transactional
  @Test
  void testFindAll() {
    List<Question> questionList = questionRepository.findAll();
    System.out.println("questionList = " + questionList);
    assertEquals(2, questionList.size());

    Question question = questionList.get(0);
    assertEquals("sbb가 무엇인가요?", question.getSubject());

  }
}