package com.mysite.sbb.answer.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerDto {

  @NotEmpty(message = "내용은 필수 항목입니다.")
  private String content;
}
