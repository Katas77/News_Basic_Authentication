package com.example.News_Basic_Authentication.repository;


import com.example.News_Basic_Authentication.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {


    Page<User> findAll(Pageable pageable);


    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByNickname(String nickname);

}
