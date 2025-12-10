package com.mysite.sbb.common;

import com.mysite.sbb.member.entity.Member;
import com.mysite.sbb.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalControllerAdvice {

    private final MemberService memberService;

    @ModelAttribute
    public void handleUserAttributes(Model model, Principal principal) {
        if (principal != null) {
            Member member = memberService.getMember(principal.getName());

            // 1. 이름 결정
            String displayName = (member.getName() != null) ? member.getName() : member.getUsername();

            String profileImage = member.getProfileImage();

            if (profileImage == null || profileImage.isEmpty()) {
                profileImage = "/images/default.png";
            }

            model.addAttribute("userName", displayName);
            model.addAttribute("userImage", profileImage); // 이제 여기엔 절대 null이 안 들어감
            model.addAttribute("email", member.getEmail()); // 이메일도 추가 (nav에 쓰임)
        }
    }
}