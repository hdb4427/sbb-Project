package com.mysite.sbb.member.entity;

import com.mysite.sbb.audit.BaseEntity;
import com.mysite.sbb.member.constant.Department;
import com.mysite.sbb.member.constant.Gender;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long id;

  @Column(unique = true, nullable = false)
  private String username;                // 이름

  private String password;                // 비밀번호

  @Column(unique = true, nullable = false)
  private String email;                   // 이메일

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private Gender gender;                  // 성별

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private Department department;          // 학과

  @Column(nullable = false)
  private Boolean registration;           // 등록 확인

//  @CreatedDate
//  private LocalDateTime created;          // 생성일

}