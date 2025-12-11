package com.mysite.sbb.member.dto;

// import com.mysite.sbb.member.constant.Department; // HTML에서 문자열을 보내므로 Enum 대신 String 사용
import com.mysite.sbb.member.constant.Gender;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {

    @NotEmpty(message = "사용자 ID는 필수 항목입니다.")
    @Size(min = 3, max = 30, message = "사용자ID는 3~30자로 입력하세요.")
    private String username;

    @NotEmpty(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 4, message = "비밀번호는 4자 이상이어야 합니다.")
    private String password1;

    @NotEmpty(message = "비밀번호 확인은 필수 항목입니다.")
    private String password2;

    @NotEmpty(message = "이름은 필수 항목입니다.")
    private String name;   // ✅ 여기 추가

    @NotEmpty(message = "이메일은 필수 항목입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotNull(message = "성별을 선택하세요.")
    private Gender gender;

    @NotEmpty(message = "운동 목표(학과)를 선택하세요.")
    private String department;

    private Boolean registration;
}
