package com.example.News_Basic_Authentication.web.mapper.impl;


import com.example.News_Basic_Authentication.model.Comment;
import com.example.News_Basic_Authentication.model.News;
import com.example.News_Basic_Authentication.model.user.User;
import com.example.News_Basic_Authentication.service.UserService;
import com.example.News_Basic_Authentication.web.dto.comment.CommentListResponse;
import com.example.News_Basic_Authentication.web.dto.news.NewsResponse;
import com.example.News_Basic_Authentication.web.dto.news.NewsResponseFindById;
import com.example.News_Basic_Authentication.web.dto.news.createNewsRequest;
import com.example.News_Basic_Authentication.web.mapper.CommentMapper;
import com.example.News_Basic_Authentication.web.mapper.NewsMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.processing.Generated;
import java.util.List;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2024-03-30T17:35:46+0300",
        comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.6.jar, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
@Primary
@AllArgsConstructor
public class NewsMapAllField implements NewsMapper {

    private final UserService userService;
    private final CommentMapper commentMapper;

    @Override
    public News requestToNews(createNewsRequest request, UserDetails userDetails) {
        if (request == null) {
            return null;
        }
        User user = userService.findByUserNickname(userDetails.getUsername());
        News.NewsBuilder news = News.builder();
        news.category(request.getCategory());
        news.text(request.getText());
        news.user(user);

        return news.build();
    }

    @Override
    public News requestNews(Long newsId, createNewsRequest request, UserDetails userDetails) {
        if (newsId == null && request == null) {
            return null;
        }
        User user = userService.findByUserNickname(userDetails.getUsername());
        News.NewsBuilder news = News.builder();
        if (request != null) {
            news.category(request.getCategory());
            news.text(request.getText());
        }
        news.id(newsId);
        news.user(user);
        return news.build();
    }

    @Override
    public NewsResponse newsToResponse(News news) {
        if (news == null) {
            return null;
        }
        NewsResponse newsResponse = new NewsResponse();
        newsResponse.setId(news.getId());
        newsResponse.setCategory(news.getCategory());
        newsResponse.setText(news.getText());
        newsResponse.setUser_id(news.getUser().getId());
        newsResponse.setNumberOfComments((long) news.getCommentList().size());
        return newsResponse;
    }

    @Override
    public NewsResponseFindById newsToResponseAllField(News news) {
        if (news == null) {
            return null;
        }
        NewsResponseFindById newsResponse = new NewsResponseFindById();
        newsResponse.setId(news.getId());
        newsResponse.setCategory(news.getCategory());
        newsResponse.setText(news.getText());
        newsResponse.setUser_id(news.getUser().getId());
        newsResponse.setNumberOfComments((long) news.getCommentList().size());
        if (!news.getCommentList().isEmpty()) {
            List<Comment> commentList = news.getCommentList();
            CommentListResponse commentListResponse = commentMapper.commentListResponseList(commentList);
            newsResponse.setCommentResponses(commentListResponse.getCommentResponses());
        }
        return newsResponse;
    }
}
