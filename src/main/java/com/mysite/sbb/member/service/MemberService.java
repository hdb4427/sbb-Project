package com.mysite.sbb.member.service;

import com.mysite.sbb.member.dto.MemberDto;
import com.mysite.sbb.member.entity.Member;
import com.mysite.sbb.member.constant.Department; // entity -> constant 로 변경
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

    public void create(@Valid MemberDto memberDto) {
        Member member = Member.builder()
                .username(memberDto.getUsername())
                .password(passwordEncoder.encode(memberDto.getPassword1()))
                .email(memberDto.getEmail())
                .gender(memberDto.getGender())
                // [수정] String을 Department Enum으로 변환
                .department(Department.valueOf(memberDto.getDepartment()))
                .registration(memberDto.getRegistration())
                .build();

        memberRepository.save(member);
    }

    public Member getMember(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다 : " + username));
        return member;
    }
}