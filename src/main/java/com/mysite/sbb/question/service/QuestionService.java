package com.mysite.sbb.question.service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.member.entity.Member;
import com.mysite.sbb.question.entity.Question;
import com.mysite.sbb.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    // ✅ category 파라미터 추가된 목록 조회
    public Page<Question> getList(int page, String kw, String category) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("created"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        // 1. 카테고리가 있고, 검색어도 있을 때
        if (category != null && !category.isEmpty()) {
            return questionRepository.findAllByCategoryAndKeyword(category, kw, pageable);
        }

        // 2. 카테고리가 없을 때 (전체 보기)
        if (kw == null || kw.trim().isEmpty()) {
            return questionRepository.findAll(pageable);
        }

        // 검색어가 있으면 전체 중에서 검색
        return questionRepository.findBySubjectContainingIgnoreCaseOrContentContainingIgnoreCase(kw, kw, pageable);
    }

    public Question getQuestion(Integer id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("question not found"));
    }

    // ✅ 질문 생성 (이미지 리사이징 적용)
    public void create(String subject, String content, Member user, String category, LocalDate recordDate, MultipartFile file) throws Exception {

        String imageString = null;

        if (file != null && !file.isEmpty()) {
            // 원본 그대로 저장하는 대신, 리사이징 메서드 호출
            imageString = resizeImage(file, 800);
        }

        Question q = Question.builder()
                .subject(subject)
                .content(content)
                .author(user)
                .category(category)
                .recordDate(recordDate)
                .thumbnail(imageString)
                .build();

        questionRepository.save(q);
    }

    // ✅ 질문 수정 (이미지 리사이징 적용)
    public void modify(Question q, String subject, String content, String category, LocalDate recordDate, MultipartFile file) throws Exception {

        q.setSubject(subject);
        q.setContent(content);
        q.setCategory(category);
        q.setRecordDate(recordDate);

        if (file != null && !file.isEmpty()) {
            // 원본 그대로 저장하는 대신, 리사이징 메서드 호출
            String imageString = resizeImage(file, 800);
            q.setThumbnail(imageString);
        }

        questionRepository.save(q);
    }

    public void delete(Question q) {
        questionRepository.delete(q);
    }

    public List<Question> getRecentQuestions(String category) {
        return questionRepository.findTop10ByCategoryOrderByCreatedDesc(category);
    }

    private String resizeImage(MultipartFile file, int targetWidth) throws IOException {

        BufferedImage originalImage = ImageIO.read(file.getInputStream());

        if (originalImage == null) {
            return null;
        }

        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // 비율 유지하며 높이 계산
        int targetHeight = (int) (originalHeight * ((double) targetWidth / originalWidth));

        // 이미지가 목표보다 작으면 원본 크기 유지
        if (originalWidth < targetWidth) {
            targetWidth = originalWidth;
            targetHeight = originalHeight;
        }

        // 리사이징 실행
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = resizedImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics.dispose();

        // JPG로 압축하여 Base64 변환
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        String formatName = "jpg"; // 용량 최적화를 위해 jpg 사용

        // 투명 배경이 있는 PNG라면 PNG 유지
        if(file.getContentType() != null && file.getContentType().contains("png")){
            formatName = "png";
        }

        ImageIO.write(resizedImage, formatName, os);
        String base64 = Base64.getEncoder().encodeToString(os.toByteArray());

        return "data:image/" + formatName + ";base64," + base64;
    }
}