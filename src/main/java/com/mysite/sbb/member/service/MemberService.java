package com.mysite.sbb.member.service;

import com.mysite.sbb.member.constant.Department;
import com.mysite.sbb.member.dto.MemberDto;
import com.mysite.sbb.member.entity.Member;
import com.mysite.sbb.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원 가입
    public void create(@Valid MemberDto memberDto) {

        // (선택) 비밀번호 체크 - 보통은 컨트롤러/Validator에서 처리하지만 여기서도 한 번 막을 수 있음
        if (!memberDto.getPassword1().equals(memberDto.getPassword2())) {
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        // department 문자열 → Enum 변환
        // ⚠️ HTML <option value="DIET"> 처럼 Enum 이름과 동일한 값을 보내야 합니다.
        Department department = Department.valueOf(memberDto.getDepartment());

        String encodedPassword = passwordEncoder.encode(memberDto.getPassword1());

        Member member = Member.builder()
                .username(memberDto.getUsername())
                .name(memberDto.getName())
                .password(encodedPassword)
                .email(memberDto.getEmail())
                .gender(memberDto.getGender())
                .department(department)
                .registration(memberDto.getRegistration())
                .build();

        memberRepository.save(member);
    }

    // username으로 회원 조회
    public Member getMember(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() ->
                        new EntityNotFoundException("해당 사용자를 찾을 수 없습니다 : " + username));
    }
}
