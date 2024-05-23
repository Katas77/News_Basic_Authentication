

package com.example.News_Basic_Authentication.controller;


import com.example.News_Basic_Authentication.aop.AllowedCommentDell;
import com.example.News_Basic_Authentication.aop.AllowedCommentUpdate;
import com.example.News_Basic_Authentication.service.CommentService;
import com.example.News_Basic_Authentication.web.dto.comment.CommentListResponse;
import com.example.News_Basic_Authentication.web.dto.comment.CommentResponse;
import com.example.News_Basic_Authentication.web.dto.comment.createCommentRequest;
import com.example.News_Basic_Authentication.web.mapper.CommentMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @GetMapping("/newsId/{newsId}")
    public ResponseEntity<CommentListResponse> findAll(@PathVariable("newsId") long newsId) {
        return ResponseEntity.ok(commentMapper.commentListResponseList(commentService.findAll(newsId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> findById(@PathVariable long id) {
        return ResponseEntity.ok(commentMapper.commentToResponse(commentService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid createCommentRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        return commentService.save(commentMapper.requestToComment(request, userDetails));
    }


    @PutMapping("/{commentId}")
    @AllowedCommentUpdate
    public ResponseEntity<String> update(@PathVariable("commentId") long commentId, @RequestBody @Valid createCommentRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        return commentService.update(commentMapper.requestToComment(commentId, request, userDetails));
    }


    @DeleteMapping("/{id}")
    @AllowedCommentDell
    public ResponseEntity<String> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return commentService.deleteById(id);
    }
}


