package com.mysite.sbb.member.service;

import com.mysite.sbb.member.constant.MemberRole;
import com.mysite.sbb.member.entity.Member;
import com.mysite.sbb.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberSecurityService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<Member> loginMember = memberRepository.findByUsername(username); // 사용자 이름(ID)으로 회원 조회
    if(loginMember.isEmpty()){
      throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다: " + username);
    }

    Member member = loginMember.get();

    // 권한 처리 
    List<GrantedAuthority> authorities = new ArrayList<>();
    if("admin".equals(username)){
      authorities.add(new SimpleGrantedAuthority(MemberRole.ADMIN.getValue())); // 관리자 권한 부여
    } else {
      authorities.add(new SimpleGrantedAuthority(MemberRole.USER.getValue())); // 일반 사용자 권한 부여
    }
    return new User(member.getUsername(), member.getPassword(), authorities); // 권한이 있는 사용자 생성
  }
}