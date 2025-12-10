package com.mysite.sbb.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile; // 추가

@Getter
@Setter
public class MemberModifyForm {

    @NotEmpty(message = "이름은 필수 항목입니다.")
    private String name;

    @NotEmpty(message = "이메일은 필수 항목입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotEmpty(message = "운동 목표를 선택해주세요.")
    private String department;

    private MultipartFile file;
}