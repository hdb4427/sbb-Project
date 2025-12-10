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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        if (kw == null || kw.trim().isEmpty()) {
            return questionRepository.findAll(pageable);
        }

        return questionRepository.findBySubjectContainingIgnoreCaseOrContentContainingIgnoreCase(
                kw, kw, pageable
        );
    }

    // ID 타입 String -> Integer 변경
    public Question getQuestion(Integer id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("question not found"));
    }

    public void create(String subject, String content, Member user,
                       String category, LocalDate recordDate, MultipartFile file) throws Exception {

        String imageString = null;

        if (file != null && !file.isEmpty()) {
            String base64 = Base64.getEncoder().encodeToString(file.getBytes());
            imageString = "data:" + file.getContentType() + ";base64," + base64;
        }

        Question q = Question.builder()
                .subject(subject)
                .content(content)
                .createDate(LocalDate.now())
                .author(user) // Member 객체 저장
                .category(category)
                .recordDate(recordDate)
                .thumbnail(imageString)
                .build();

        questionRepository.save(q);
    }

    public void modify(Question q, String subject, String content,
                       String category, LocalDate recordDate, MultipartFile file) throws Exception {

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
}