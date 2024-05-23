package com.example.News_Basic_Authentication.web.mapper.impl;


import com.example.News_Basic_Authentication.model.Comment;
import com.example.News_Basic_Authentication.model.News;
import com.example.News_Basic_Authentication.model.user.User;
import com.example.News_Basic_Authentication.service.NewsService;
import com.example.News_Basic_Authentication.service.UserService;
import com.example.News_Basic_Authentication.web.dto.comment.CommentResponse;
import com.example.News_Basic_Authentication.web.dto.comment.createCommentRequest;
import com.example.News_Basic_Authentication.web.mapper.CommentMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.processing.Generated;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2024-03-30T17:35:46+0300",
        comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.6.jar, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
@Primary
@AllArgsConstructor
public class CommentMapAllField implements CommentMapper {
    private final UserService userService;
    private final NewsService newsService;

    @Override
    public Comment requestToComment(createCommentRequest request, UserDetails userDetails) {
        if (request == null) {
            return null;
        }
        User user = userService.findByUserNickname(userDetails.getUsername());
        News news = newsService.findById(request.getNews_id());
        Comment.CommentBuilder comment = Comment.builder();
        comment.text(request.getText());
        comment.user(user);
        comment.news(news);
        return comment.build();
    }

    @Override
    public Comment requestToComment(Long commentId, createCommentRequest request, UserDetails userDetails) {
        if (commentId == null && request == null) {
            return null;
        }
        Comment.CommentBuilder comment = Comment.builder();
        if (request != null) {
            comment.text(request.getText());
        }
        comment.id(commentId);
        User user = userService.findByUserNickname(userDetails.getUsername());
        News news = newsService.findById(request.getNews_id());
        comment.text(request.getText());
        comment.user(user);
        comment.news(news);
        return comment.build();
    }

    @Override
    public CommentResponse commentToResponse(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(comment.getId());
        commentResponse.setText(comment.getText());
        commentResponse.setUser_id(comment.getUser().getId());
        commentResponse.setNews_id(comment.getNews().getId());
        return commentResponse;
    }
}
