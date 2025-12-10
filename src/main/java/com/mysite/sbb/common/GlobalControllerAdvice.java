package com.mysite.sbb.common;

import com.mysite.sbb.member.entity.Member;
import com.mysite.sbb.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@RequiredArgsConstructor
@ControllerAdvice // 모든 컨트롤러 전역에서 동작
public class GlobalControllerAdvice {

    private final MemberService memberService;

    @ModelAttribute
    public void handleUserAttributes(Model model, Principal principal) {
        if (principal != null) {
            Member member = memberService.getMember(principal.getName());

            String displayName = (member.getName() != null) ? member.getName() : member.getUsername();

            String profileImage = "/images/default_profile.png";
            model.addAttribute("userName", displayName);
            model.addAttribute("userImage", profileImage);
        } else {
            // 로그인하지 않은 경우 (null 처리는 HTML th:if에서 하므로 여기선 생략 가능)
            model.addAttribute("userName", null);
        }
    }
}