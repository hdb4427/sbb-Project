package com.mysite.sbb.common;

import com.mysite.sbb.question.entity.Question;
import com.mysite.sbb.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor // ✅ 생성자 주입을 위해 추가 필수
public class IndexController {

  private final QuestionService questionService; // ✅ 서비스 연결

  @GetMapping("/")
  public String index(Model model) {
    // 각 카테고리별 최신글 10개씩 조회
    List<Question> healthList = questionService.getRecentQuestions("운동");
    List<Question> dietList = questionService.getRecentQuestions("식단");
    List<Question> eyeBodyList = questionService.getRecentQuestions("눈바디");
    List<Question> freeList = questionService.getRecentQuestions("자유");

    // 화면(HTML)으로 전달
    model.addAttribute("healthList", healthList);
    model.addAttribute("dietList", dietList);
    model.addAttribute("eyeBodyList", eyeBodyList);
    model.addAttribute("freeList", freeList);

    return "index";
  }
}