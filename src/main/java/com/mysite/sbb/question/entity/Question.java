package com.mysite.sbb.question.entity;

import com.mysite.sbb.answer.entity.Answer;
import com.mysite.sbb.audit.BaseEntity; // 👈 이 import가 있어야 합니다.
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
// 👇 여기에 extends BaseEntity가 꼭 있어야 'created' 필드를 인식합니다!
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String category;

    private LocalDate recordDate;

    @ManyToOne
    private Member author;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    @Column(columnDefinition = "LONGTEXT")
    private String thumbnail;
}