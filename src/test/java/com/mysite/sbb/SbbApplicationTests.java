package com.mysite.sbb;

import com.mysite.sbb.member.constant.Department;
import com.mysite.sbb.member.constant.Gender;
import com.mysite.sbb.member.entity.Member;
import com.mysite.sbb.member.repository.MemberRepository;
import com.mysite.sbb.question.entity.Question;
import com.mysite.sbb.question.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Random;

@SpringBootTest
class SbbApplicationTests {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void generateDummyData() {
        // 1. 임시 회원 10명 생성
        for (int i = 1; i <= 10; i++) {
            String username = "user" + i;

            // 이미 있으면 생성 스킵
            if (memberRepository.findByUsername(username).isPresent()) {
                continue;
            }

            Department dept = (i % 2 == 0) ? Department.DIET : Department.BULKUP;
            Gender gender = (i % 2 == 0) ? Gender.MALE : Gender.FEMALE;

            Member member = Member.builder()
                    .username(username)
                    .password(passwordEncoder.encode("1234")) // 비밀번호 1234
                    .name("테스트유저" + i)
                    .email("user" + i + "@test.com")
                    .gender(gender)
                    .department(dept)
                    .registration(true)
                    .profileImage(null) // 프사는 일단 비움
                    .build();

            memberRepository.save(member);
        }

        // 2. 임시 질문글 50개 생성
        Member author = memberRepository.findByUsername("user1").orElse(null);
        String[] categories = {"운동", "식단", "눈바디", "자유"};
        Random random = new Random();

        for (int i = 1; i <= 50; i++) {
            String category = categories[random.nextInt(categories.length)]; // 카테고리 랜덤

            Question q = Question.builder()
                    .subject("테스트 게시글입니다. [" + i + "]")
                    .content("임시로 생성된 테스트 내용입니다. 내용은 조금 길게 작성해봅니다. " + i)
                    .category(category)
                    .author(author) // user1이 작성한 것으로 설정
                    .recordDate(LocalDate.now().minusDays(random.nextInt(30))) // 최근 30일 내 랜덤 날짜
                    .thumbnail(null)
                    .build();

            questionRepository.save(q);
        }
    }
}