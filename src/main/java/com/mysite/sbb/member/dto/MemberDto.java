package com.mysite.sbb.member.dto;

// import com.mysite.sbb.member.constant.Department; // [수정] HTML에서 문자열을 보내므로 Enum 대신 String 사용
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

    @NotEmpty(message = "이메일은 필수 항목입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotNull(message = "성별을 선택하세요.")
    private Gender gender;

    // [수정 1] 학과(Department) Enum -> String으로 변경
    // 이유: HTML에서 "체중감량학과", "근력증가학과" 같은 한글 문자열을 보내고 있어서 Enum과 맞지 않아 에러가 납니다.
    @NotEmpty(message = "운동 목표(학과)를 선택하세요.")
    private String department;

    // [수정 2] @AssertTrue 제거 (필수 -> 선택)
    // 이제 체크하지 않아도 가입이 가능합니다. (null일 수 있으므로 Boolean 타입 유지)
    private Boolean registration;
}