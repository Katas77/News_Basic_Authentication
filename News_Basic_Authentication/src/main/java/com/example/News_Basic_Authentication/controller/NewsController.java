

package com.example.News_Basic_Authentication.controller;


import com.example.News_Basic_Authentication.aop.AllowedNewsDell;
import com.example.News_Basic_Authentication.aop.AllowedNewsUpdate;
import com.example.News_Basic_Authentication.service.NewsService;
import com.example.News_Basic_Authentication.web.dto.news.NewsListResponse;
import com.example.News_Basic_Authentication.web.dto.news.NewsResponseFindById;
import com.example.News_Basic_Authentication.web.dto.news.createNewsRequest;
import com.example.News_Basic_Authentication.web.mapper.NewsMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    private final NewsMapper newsMapper;

    @GetMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<NewsListResponse> findAll(@PathVariable int pageNumber, @PathVariable int pageSize) {
        return ResponseEntity.ok(newsMapper.newsListResponseList(newsService.findAll(pageNumber, pageSize)));
    }

    @GetMapping("/filter/category/{category}")
    public ResponseEntity<NewsListResponse> filterByCategory(@PathVariable String category) {
        return ResponseEntity.ok(newsMapper.newsListResponseList(newsService.filterByCategory(category)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("/filter")
    public ResponseEntity<NewsListResponse> filterByUser_id(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(newsMapper.newsListResponseList(newsService.filterByUser_id(userDetails)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsResponseFindById> findById(@PathVariable long id) {
        return ResponseEntity.ok(newsMapper.newsToResponseAllField(newsService.findById(id)));
    }

    @PostMapping("/admin")
    public ResponseEntity<String> create(@RequestBody @Valid createNewsRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        return newsService.save(newsMapper.requestToNews(request, userDetails));
    }

    @AllowedNewsUpdate
    @PutMapping("/{newsId}")
    public ResponseEntity<String> update(@PathVariable("newsId") long newsId, @Valid @RequestBody createNewsRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        return newsService.update(newsMapper.requestNews(newsId, request, userDetails));
    }

    @AllowedNewsDell
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return newsService.deleteById(id);
    }


}


/*
Аннотации @PreAuthorize и @PostAuthorize обеспечивают управление доступом на основе выражений. Итак, предикаты можно писать с помощью SpEL (Spring Expression Language) .
Аннотация @PreAuthorize проверяет данное выражение перед входом в метод , тогда как аннотация @PostAuthorize проверяет его после выполнения метода и может изменить результат.
аннотация @AuthenticationPrincipal. Её использование позволит вам внедрять данные UserDetails непосредственно в аргументы методов контроллера.*/