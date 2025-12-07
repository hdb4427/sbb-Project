package com.mysite.sbb.question.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Base64;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.member.entity.Member;
import com.mysite.sbb.question.entity.Question;
import com.mysite.sbb.question.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("created"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.questionRepository.findAllByKeyword(kw, pageable);
    }

    public Question getQuestion(Long id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    // 질문 생성 (이미지 포함)
    public void create(String subject, String content, Member user,
                       String category, LocalDate recordDate, MultipartFile file) throws IOException {

        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setAuthor(user);
        q.setCategory(category);
        q.setRecordDate(recordDate);

        // 이미지 처리 (Base64) -> thumbnail 필드 사용
        if (file != null && !file.isEmpty()) {
            String contentType = file.getContentType();
            byte[] fileBytes = file.getBytes();
            String base64Encoded = Base64.getEncoder().encodeToString(fileBytes);
            String finalThumbnail = "data:" + contentType + ";base64," + base64Encoded;

            q.setThumbnail(finalThumbnail); // [수정] thumbnail로 통일
        }

        this.questionRepository.save(q);
    }

    // 질문 수정 (이미지, 카테고리, 날짜 포함)
    public void modify(Question question, String subject, String content,
                       String category, LocalDate recordDate, MultipartFile file) throws IOException {

        question.setSubject(subject);
        question.setContent(content);
        question.setCategory(category);
        question.setRecordDate(recordDate);

        // 이미지 수정 로직: 새 파일이 있을 때만 교체
        if (file != null && !file.isEmpty()) {
            String contentType = file.getContentType();
            byte[] fileBytes = file.getBytes();
            String base64Encoded = Base64.getEncoder().encodeToString(fileBytes);
            String finalThumbnail = "data:" + contentType + ";base64," + base64Encoded;

            question.setThumbnail(finalThumbnail); // [수정] thumbnail로 통일
        }

        this.questionRepository.save(question);
    }

    public void delete(Question question) {
        this.questionRepository.delete(question);
    }

    public void vote(Question question, Member siteUser) {
        this.questionRepository.save(question);
    }
}