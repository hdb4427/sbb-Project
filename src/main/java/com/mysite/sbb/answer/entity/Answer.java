package com.mysite.sbb.answer.entity;

import com.mysite.sbb.audit.BaseEntity; // ✅ 이 import 필수!
import com.mysite.sbb.member.entity.Member;
import com.mysite.sbb.question.entity.Question;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 👇 여기에 extends BaseEntity가 반드시 있어야 'created'를 인식합니다.
public class Answer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    private Question question;

    @ManyToOne
    private Member author;
}