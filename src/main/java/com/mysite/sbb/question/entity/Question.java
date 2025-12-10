package com.mysite.sbb.question.entity;

import com.mysite.sbb.answer.entity.Answer;
import com.mysite.sbb.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Integer로 변경

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDate createDate;

    private String category;

    private LocalDate recordDate;

    // 작성자 (Member 객체와 연결)
    @ManyToOne
    private Member author;

    // 답변 리스트 (1:N 관계)
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    // Base64 이미지는 길이가 매우 길 수 있으므로 TEXT 타입 지정
    @Column(columnDefinition = "LONGTEXT")
    private String thumbnail;
}