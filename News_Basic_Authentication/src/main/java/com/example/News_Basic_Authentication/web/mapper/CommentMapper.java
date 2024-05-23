package com.example.News_Basic_Authentication.web.mapper;


import com.example.News_Basic_Authentication.model.Comment;
import com.example.News_Basic_Authentication.web.dto.comment.CommentListResponse;
import com.example.News_Basic_Authentication.web.dto.comment.CommentResponse;
import com.example.News_Basic_Authentication.web.dto.comment.createCommentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {
    Comment requestToComment(createCommentRequest request, UserDetails userDetails);

    @Mapping(source = "commentId", target = "id")
    Comment requestToComment(Long commentId, createCommentRequest request, UserDetails userDetails);

    CommentResponse commentToResponse(Comment comment);

    default CommentListResponse commentListResponseList(List<Comment> comments) {
        CommentListResponse response = new CommentListResponse();
        response.setCommentResponses(comments.stream().map(this::commentToResponse).collect(Collectors.toList()));
        return response;
    }
}
