package com.mysite.sbb.question.dto;

import com.mysite.sbb.member.dto.MemberDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDto {

  @NotEmpty(message = "제목은 필수항목입니다.")
  @Size(max = 200, message = "제목은 200자 이내로 입력해주세요.")
  private String subject;

  @NotEmpty(message = "내용은 필수항목입니다.")
  private String content;
}
