package com.example.News_Basic_Authentication.service;

import com.example.News_Basic_Authentication.model.Comment;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CommentService {
    List<Comment> findAll(Long newsId);

    Comment findById(Long id);

    ResponseEntity<String> save(Comment comment);

    ResponseEntity<String>  update(Comment comment);
    void dellAll();
    public ResponseEntity<String> deleteById(Long id);

}
