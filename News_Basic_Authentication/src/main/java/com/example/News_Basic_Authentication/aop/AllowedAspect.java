
package com.example.News_Basic_Authentication.aop;


import com.example.News_Basic_Authentication.exception.UnauthorizedAccessException;
import com.example.News_Basic_Authentication.model.user.RoleType;
import com.example.News_Basic_Authentication.model.user.User;
import com.example.News_Basic_Authentication.service.CommentService;
import com.example.News_Basic_Authentication.service.NewsService;
import com.example.News_Basic_Authentication.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Slf4j
@AllArgsConstructor
public class AllowedAspect {

    private final UserService userService;
    private final NewsService newsService;
    private final CommentService commentService;

    @Before("@annotation(AllowedNewsUpdate)")
    public void checkNewsUpdate(JoinPoint joinPoint) throws UnauthorizedAccessException {
        User user = joinPointUser(joinPoint);
        long newsId = joinPointId(joinPoint);
        if (newsService.findById(newsId).getUser().getId() != user.getId()) {
            throw new UnauthorizedAccessException("Only the user who created it can update the news");
        }

    }

    @Before("@annotation(AllowedNewsDell)")
    public void checkNewsDell(JoinPoint joinPoint) throws UnauthorizedAccessException {
        User user = joinPointUser(joinPoint);
        long newsId = joinPointId(joinPoint);
        if (verificationROLE_USER(user) & newsService.findById(newsId).getUser().getId() != user.getId()) {
            throw new UnauthorizedAccessException("A user who has only ROLE_USER has the right to delete only the news that he himself created");
        }

    }

    @Before("@annotation(AllowedCommentUpdate)")
    public void checkCommentUpdate(JoinPoint joinPoint) throws UnauthorizedAccessException {
        User user = joinPointUser(joinPoint);
        long commentId = joinPointId(joinPoint);
        if (commentService.findById(commentId).getUser().getId() != user.getId()) {
            throw new UnauthorizedAccessException("Only the user who created it can update a comment.");
        }

    }

    @Before("@annotation(AllowedCommentDell)")
    public void checkCommentDell(JoinPoint joinPoint) throws UnauthorizedAccessException {
        User user = joinPointUser(joinPoint);
        long commentId = joinPointId(joinPoint);
        if (verificationROLE_USER(user) & commentService.findById(commentId).getUser().getId() != user.getId()) {
            throw new UnauthorizedAccessException("A user who has only ROLE_USER has the right to delete only the comment that he himself created");
        }


    }

    @Before("@annotation(AllowedUserUpdate)")
    public void updateUser(JoinPoint joinPoint) throws UnauthorizedAccessException {
        User user = joinPointUser(joinPoint);
        long usersId = joinPointId(joinPoint);
        if (verificationROLE_USER(user) & usersId != user.getId()) {
            throw new UnauthorizedAccessException("A user who only has ROLE_USER has the right to update only information about himself");
        }

    }

    @Before("@annotation( AllowedUserDell)")
    public void dellUser(JoinPoint joinPoint) throws UnauthorizedAccessException {
        User user = joinPointUser(joinPoint);
        long usersId = joinPointId(joinPoint);
        if (verificationROLE_USER(user) & usersId != user.getId()) {
            throw new UnauthorizedAccessException("A user who only has ROLE_USER cannot delete other users' profiles");
        }

    }

    @Before("@annotation(AllowedUserFindById)")
    public void findByIdUser(JoinPoint joinPoint) {
        String userName = "";
        Object[] objects = joinPoint.getArgs();
        for (Object object : objects) {
            if (object instanceof UserDetails) {
                {
                    userName = ((UserDetails) object).getUsername();
                }
            }
        }
        User user = userService.findByUserNickname(userName);
        if (verificationROLE_USER(user)) {
            userService.setCheck(true);
        }

    }

    public boolean verificationROLE_USER(User user) {
        return user.getRoles().size() == 1 & user.getRoles().get(0).getAuthority().equals(RoleType.ROLE_USER);
    }

    public User joinPointUser(JoinPoint joinPoint) {
        String userName = "";
        Object[] objects = joinPoint.getArgs();
        for (Object object : objects) {
            if (object instanceof UserDetails) {
                {
                    userName = ((UserDetails) object).getUsername();
                }
            }
        }
        return userService.findByUserNickname(userName);
    }

    public Long joinPointId(JoinPoint joinPoint) {
        long id = 0L;
        Object[] objects = joinPoint.getArgs();
        for (Object object : objects) {
            if (object instanceof Long) {
                id = (Long) object;
            }
        }
        return id;
    }


}






