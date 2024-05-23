package com.example.News_Basic_Authentication.repository;


import com.example.News_Basic_Authentication.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {


}
