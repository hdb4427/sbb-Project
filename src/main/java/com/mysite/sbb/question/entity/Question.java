package com.mysite.sbb.question.entity;

import com.mysite.sbb.answer.entity.Answer;
import com.mysite.sbb.audit.BaseEntity;
import com.mysite.sbb.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(exclude = "answerList")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @Column(length = 200, nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> answerList;

    // 질문 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member author;

    // 1. 카테고리 (운동, 식단, 눈바디 등)
    @Column(length = 20)
    private String category;

    // [수정] 2. 인증샷 (경로가 아니라 이미지 자체를 텍스트로 저장)
    // MariaDB에서 아주 긴 텍스트를 저장하기 위해 LONGTEXT 사용
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String thumbnail;

    // 3. 수행 날짜
    private LocalDate recordDate;
}