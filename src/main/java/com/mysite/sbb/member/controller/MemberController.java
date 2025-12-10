package com.mysite.sbb.member.controller;

import com.mysite.sbb.member.dto.MemberDto;
import com.mysite.sbb.member.dto.MemberModifyForm;
import com.mysite.sbb.member.entity.Member;
import com.mysite.sbb.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
  private final MemberService memberService;

  @GetMapping("/signup")
  public String signUp(Model model) {
    model.addAttribute("memberDto", new MemberDto());
    return "member/signup"; // signup.html 파일을 반환
  }

  @PostMapping("/signup")
  public String signUp(@Valid MemberDto memberDto, BindingResult bindingResult, Model model) {
    log.info("=====> memberDto: {}", memberDto);

    if (bindingResult.hasErrors()) {
      return "member/signup";
    }

    if (!memberDto.getPassword1().equals(memberDto.getPassword2())) {
      // 필드명, 오류 코드, 오류 메시지
      bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
      return "member/signup"; // 스크립트 처리
    }

    try {
      memberService.create(memberDto);
    } catch(DataIntegrityViolationException e){
      log.info("=============================회원가입 실패: 이미 등록된 사용자입니다.");
      model.addAttribute("errorMessage","이미 등록된 사용자입니다.");
      return "member/signup";
    } catch(Exception e){
      log.info("=============================회원가입 실패: " + e.getMessage());
      model.addAttribute("errorMessage", e.getMessage());
      return "member/signup";
    }

    return "redirect:/member/login";
  }

  @GetMapping("/login")
  public String login() {
    return "member/login";
  }

  @GetMapping(value = "/login/error")
  public String loginError(Model model){
    model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
    return "/member/login";
  }

  @GetMapping("/logout")
  public String performLogout(HttpServletRequest request, HttpServletResponse response) {
    log.info("===============> logout");
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      new SecurityContextLogoutHandler().logout(request, response, authentication);
    }
    return "redirect:/";
  }

    @PreAuthorize("isAuthenticated()") // 로그인 한 사람만 접근 가능
    @GetMapping("/setting")
    public String setting(MemberModifyForm memberModifyForm, Principal principal) {
      // 1. 현재 로그인한 사용자 정보를 가져옴
      Member member = memberService.getMember(principal.getName());

      // 2. 폼에 기존 정보를 채워서 화면으로 보냄
      memberModifyForm.setName(member.getName());
      memberModifyForm.setEmail(member.getEmail());
      memberModifyForm.setDepartment(member.getDepartment().name()); // Enum -> String

      return "member/setting";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/setting")
    public String setting(@Valid MemberModifyForm memberModifyForm,
                          BindingResult bindingResult,
                          Principal principal) throws IOException { // Exception 추가 필요

      if (bindingResult.hasErrors()) {
        return "member/setting";
      }

      Member member = memberService.getMember(principal.getName());

      // ✅ memberModifyForm.getFile()을 추가로 전달
      memberService.modify(member,
              memberModifyForm.getName(),
              memberModifyForm.getEmail(),
              memberModifyForm.getDepartment(),
              memberModifyForm.getFile()); // 여기서 파일 전달

      return "redirect:/";
    }
  }
