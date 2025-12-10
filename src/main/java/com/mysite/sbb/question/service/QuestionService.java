package com.mysite.sbb.question.service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.member.entity.Member;
import com.mysite.sbb.question.entity.Question;
import com.mysite.sbb.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    // ✅ category 파라미터 추가
    public Page<Question> getList(int page, String kw, String category) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("created"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        // 1. 카테고리가 있고, 검색어도 있을 때
        if (category != null && !category.isEmpty()) {
            return questionRepository.findAllByCategoryAndKeyword(category, kw, pageable);
        }

        // 2. 카테고리가 없을 때 (전체 보기)
        // 검색어가 없으면 전체 조회
        if (kw == null || kw.trim().isEmpty()) {
            return questionRepository.findAll(pageable);
        }

        // 검색어가 있으면 전체 중에서 검색
        return questionRepository.findBySubjectContainingIgnoreCaseOrContentContainingIgnoreCase(kw, kw, pageable);
    }

    // ... (나머지 create, modify, delete 메서드는 기존 그대로 유지)
    public Question getQuestion(Integer id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("question not found"));
    }

    // (기존 create, modify 코드들 생략...)
    public void create(String subject, String content, Member user, String category, LocalDate recordDate, MultipartFile file) throws Exception {
        // ... (내용 유지) ...
        String imageString = null;
        if (file != null && !file.isEmpty()) {
            String base64 = Base64.getEncoder().encodeToString(file.getBytes());
            imageString = "data:" + file.getContentType() + ";base64," + base64;
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

    public void modify(Question q, String subject, String content, String category, LocalDate recordDate, MultipartFile file) throws Exception {
        // ... (내용 유지) ...
        q.setSubject(subject);
        q.setContent(content);
        q.setCategory(category);
        q.setRecordDate(recordDate);

        if (file != null && !file.isEmpty()) {
            String base64 = Base64.getEncoder().encodeToString(file.getBytes());
            q.setThumbnail("data:" + file.getContentType() + ";base64," + base64);
        }
        questionRepository.save(q);
    }

    public void delete(Question q) {
        questionRepository.delete(q);
    }

    public List<Question> getRecentQuestions(String category) {
        return questionRepository.findTop10ByCategoryOrderByCreatedDesc(category);
    }
}