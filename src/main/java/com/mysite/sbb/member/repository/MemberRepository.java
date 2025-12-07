package com.mysite.sbb.member.repository;

import com.mysite.sbb.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long>{
  Optional<Member> findByUsername(String username); // 로그인이 가능한 사용자 찾기 --> Role 추가
}
