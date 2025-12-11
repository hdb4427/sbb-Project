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

    public Page<Question> getList(int page, String kw, String category) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("created"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        if (category != null && !category.isEmpty()) {
            return questionRepository.findAllByCategoryAndKeyword(category, kw, pageable);
        }

        if (kw == null || kw.trim().isEmpty()) {
            return questionRepository.findAll(pageable);
        }

        return questionRepository.findBySubjectContainingIgnoreCaseOrContentContainingIgnoreCase(kw, kw, pageable);
    }

    public Question getQuestion(Integer id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("question not found"));
    }

    public void create(String subject, String content, Member user, String category, LocalDate recordDate, MultipartFile file) throws Exception {

        String imageString = null;

        if (file != null && !file.isEmpty()) {
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

    public void modify(Question q, String subject, String content, String category, LocalDate recordDate, MultipartFile file) throws Exception {

        q.setSubject(subject);
        q.setContent(content);
        q.setCategory(category);
        q.setRecordDate(recordDate);

        if (file != null && !file.isEmpty()) {
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

        if(file.getContentType() != null && file.getContentType().contains("png")){
            formatName = "png";
        }

        ImageIO.write(resizedImage, formatName, os);
        String base64 = Base64.getEncoder().encodeToString(os.toByteArray());

        return "data:image/" + formatName + ";base64," + base64;
    }
}