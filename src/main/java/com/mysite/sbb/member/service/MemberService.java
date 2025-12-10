package com.mysite.sbb.member.service;

import com.mysite.sbb.member.constant.Department;
import com.mysite.sbb.member.dto.MemberDto;
import com.mysite.sbb.member.entity.Member;
import com.mysite.sbb.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile; // ✅ 필수 추가

import javax.imageio.ImageIO; // ✅ 필수 추가
import java.awt.*;            // ✅ 필수 추가
import java.awt.image.BufferedImage; // ✅ 필수 추가
import java.io.ByteArrayOutputStream; // ✅ 필수 추가
import java.io.IOException;   // ✅ 필수 추가
import java.util.Base64;      // ✅ 필수 추가

@Service
@Validated
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원 가입
    public void create(@Valid MemberDto memberDto) {
        if (!memberDto.getPassword1().equals(memberDto.getPassword2())) {
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        Department department = Department.valueOf(memberDto.getDepartment());
        String encodedPassword = passwordEncoder.encode(memberDto.getPassword1());

        Member member = Member.builder()
                .username(memberDto.getUsername())
                .name(memberDto.getName())
                .password(encodedPassword)
                .email(memberDto.getEmail())
                .gender(memberDto.getGender())
                .department(department)
                .registration(memberDto.getRegistration())
                .build();

        memberRepository.save(member);
    }

    // username으로 회원 조회
    public Member getMember(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() ->
                        new EntityNotFoundException("해당 사용자를 찾을 수 없습니다 : " + username));
    }

    // 회원 정보 수정
    public void modify(Member member, String name, String email, String departmentStr, MultipartFile file) throws IOException {
        member.setName(name);
        member.setEmail(email);

        Department department = Department.valueOf(departmentStr);
        member.setDepartment(department);

        // ✅ 프로필 사진 변경 로직
        if (file != null && !file.isEmpty()) {
            String imageString = resizeImage(file, 200);
            member.setProfileImage(imageString);
        }

        memberRepository.save(member);
    }

    // ✅ 이미지 리사이징 유틸 메서드
    private String resizeImage(MultipartFile file, int targetWidth) throws IOException {
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        if (originalImage == null) return null;

        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        int targetHeight = (int) (originalHeight * ((double) targetWidth / originalWidth));

        if (originalWidth < targetWidth) {
            targetWidth = originalWidth;
            targetHeight = originalHeight;
        }

        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = resizedImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics.dispose();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        String formatName = "jpg";
        if (file.getContentType() != null && file.getContentType().contains("png")) {
            formatName = "png";
        }
        ImageIO.write(resizedImage, formatName, os);
        String base64 = Base64.getEncoder().encodeToString(os.toByteArray());
        return "data:image/" + formatName + ";base64," + base64;
    }
}