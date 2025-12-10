package com.mysite.sbb.question.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class QuestionForm {
    @NotEmpty(message="제목은 필수항목입니다.")
    private String subject;

    @NotEmpty(message="내용은 필수항목입니다.")
    private String content;

    @NotEmpty(message = "카테고리를 선택해주세요.")
    private String category; // 운동, 식단 등

    @NotNull(message = "날짜를 선택해주세요.")
    private LocalDate recordDate; // 운동한 날짜
}