package com.example.News_Basic_Authentication.service.impl;


import com.example.News_Basic_Authentication.model.Comment;
import com.example.News_Basic_Authentication.repository.CommentRepository;
import com.example.News_Basic_Authentication.service.CommentService;
import com.example.News_Basic_Authentication.utils.BeanUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final NewsServiceImpl newsService;


    @Override
    public List<Comment> findAll(Long newsId) {
        return newsService.findById(newsId).getCommentList();
    }

    @Override
    public Comment findById(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Comment with ID {0} not found", id)));
    }

    @Override
    public ResponseEntity<String> save(Comment comment) {
        commentRepository.save(comment);
        return ResponseEntity.ok(MessageFormat.format("Comment with Text -    {0} save", comment.getText()));
    }

    @Transactional
    @Override
    public ResponseEntity<String> update(Comment comment) {
        Optional<Comment> existedComment = commentRepository.findById(comment.getId());
        if (existedComment.isPresent()) {
            BeanUtils.copyNonNullProperties(comment, existedComment.get());
            save(existedComment.get());
            return ResponseEntity.ok(MessageFormat.format("Comment with ID {0} updated", comment.getId()));
        } else {
            return ResponseEntity.ok(MessageFormat.format("News with ID {0} not found", comment.getId()));
        }
    }


    @Override
    public void dellAll() {
        commentRepository.deleteAll();
    }

    @Transactional
    @Override
    public ResponseEntity<String> deleteById(Long id) {
        Optional<Comment> newsRepositoryById = commentRepository.findById(id);
        if (newsRepositoryById.isEmpty()) {
            return ResponseEntity.ok(MessageFormat.format("Comment with ID {0} not found", id));
        } else {
            commentRepository.deleteById(id);
            return ResponseEntity.ok(MessageFormat.format("Comment with ID {0} deleted", id));
        }
    }


}
