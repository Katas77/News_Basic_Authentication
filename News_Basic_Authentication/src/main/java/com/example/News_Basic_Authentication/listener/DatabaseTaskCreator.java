


package com.example.News_Basic_Authentication.listener;


import com.example.News_Basic_Authentication.model.Comment;
import com.example.News_Basic_Authentication.model.News;
import com.example.News_Basic_Authentication.model.user.RoleType;
import com.example.News_Basic_Authentication.model.user.User;
import com.example.News_Basic_Authentication.service.CommentService;
import com.example.News_Basic_Authentication.service.NewsService;
import com.example.News_Basic_Authentication.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseTaskCreator {
    private final CommentService commentService;
    private final NewsService newsService;
    private final UserService userService;


    @EventListener(ApplicationStartedEvent.class)
    public void createTaskData() {
        log.debug("Calling DatabaseTaskCreator->createTaskData...");
        ArrayList<News> newsArrayList = new ArrayList<>();
        ArrayList<Comment> commentArrayList = new ArrayList<>();

        User petrov = User.builder().nickname("Ivan").build();
        News spartak = News.builder().user(petrov).text("Spartak lost to Dynamo").category("Sport").build();
        News afrika = News.builder().user(petrov).text("Vegetation is represented by sugar cane, coconut palm, banana, coffee tree, and cloves.").category("Nature").build();
        newsArrayList.add(spartak);
        newsArrayList.add(afrika);

        Comment petrovSpartak = Comment.builder().user(petrov).text("Bad or good ? i don't know").news(spartak).build();
        Comment petrovAfrika = Comment.builder().user(petrov).text("I love nature").news(afrika).build();
        commentArrayList.add(petrovAfrika);
        commentArrayList.add(petrovAfrika);
        petrov.setNewstList(newsArrayList);
        petrov.setCommentList(commentArrayList);
        petrov.setPassword("2222");
        userService.create(petrov, RoleType.ROLE_USER);
        newsService.save(spartak);
        newsService.save(afrika);
        commentService.save(petrovAfrika);
        commentService.save(petrovSpartak);
    }
}





