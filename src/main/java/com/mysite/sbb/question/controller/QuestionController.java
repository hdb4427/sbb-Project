package com.mysite.sbb.question.controller;

import com.mysite.sbb.answer.dto.AnswerDto;
import com.mysite.sbb.member.entity.Member;
import com.mysite.sbb.member.service.MemberService;
import com.mysite.sbb.question.dto.QuestionDto; // 필요 없다면 삭제 가능
import com.mysite.sbb.question.dto.QuestionForm;
import com.mysite.sbb.question.entity.Question;
import com.mysite.sbb.question.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final MemberService memberService;

    // 질문 목록
    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        Page<Question> paging = questionService.getList(page, keyword);
        model.addAttribute("paging", paging);
        model.addAttribute("keyword", keyword);
        return "question/list";
    }

    // 질문 상세
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model, AnswerDto answerDto) {
        Question question = questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question/detail";
    }

    // 질문 등록 폼 이동
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String createQuestion(Model model) {
        model.addAttribute("questionForm", new QuestionForm());
        return "question/inputForm";
    }

    // 질문 등록 처리
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createQuestion(@Valid QuestionForm questionForm,
                                 BindingResult bindingResult,
                                 Principal principal,
                                 @RequestParam(value = "file", required = false) MultipartFile file) throws Exception {

        if (bindingResult.hasErrors()) {
            return "question/inputForm";
        }

        Member member = memberService.getMember(principal.getName());

        questionService.create(
                questionForm.getSubject(),
                questionForm.getContent(),
                member,
                questionForm.getCategory(),
                questionForm.getRecordDate(),
                file
        );

        return "redirect:/question/list";
    }

    // [수정 3] 질문 수정 폼 이동 (QuestionDto -> QuestionForm 변경)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modifyQuestion(@PathVariable("id") Long id,
                                 QuestionForm questionForm, // Form 사용
                                 Principal principal) {

        Question question = questionService.getQuestion(id);

        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        // 기존 데이터를 폼에 채워넣기
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        questionForm.setCategory(question.getCategory());     // 카테고리 불러오기
        questionForm.setRecordDate(question.getRecordDate()); // 날짜 불러오기

        return "question/inputForm";
    }

    // [수정 4] 질문 수정 처리 (파일 업로드 및 새 필드 반영)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modifyQuestion(@PathVariable("id") Long id,
                                 @Valid QuestionForm questionForm, // Form 사용
                                 BindingResult bindingResult,
                                 Principal principal,
                                 @RequestParam(value = "file", required = false) MultipartFile file) throws Exception { // 파일 추가

        if (bindingResult.hasErrors()) {
            return "question/inputForm";
        }

        Question question = questionService.getQuestion(id);

        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        // Service의 modify 메서드 호출 (인자 6개로 확장 필요)
        questionService.modify(
                question,
                questionForm.getSubject(),
                questionForm.getContent(),
                questionForm.getCategory(),
                questionForm.getRecordDate(),
                file
        );

        return "redirect:/question/detail/" + id;
    }

    // 질문 삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deleteQuestion(@PathVariable("id") Long id, Principal principal) {

        Question question = questionService.getQuestion(id);

        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }

        questionService.delete(question);

        return "redirect:/";
    }
}