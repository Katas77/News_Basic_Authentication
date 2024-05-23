package com.example.News_Basic_Authentication.service.impl;


import com.example.News_Basic_Authentication.model.News;
import com.example.News_Basic_Authentication.repository.NewsRepository;
import com.example.News_Basic_Authentication.repository.UserRepository;
import com.example.News_Basic_Authentication.service.NewsService;
import com.example.News_Basic_Authentication.utils.BeanUtils;
import com.example.News_Basic_Authentication.web.dto.news.NewsFilter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final UserRepository userRepository;


    @Override
    public List<News> findAll(int pageNumber, int pageSize) {
        return newsRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
    }

    @Override
    public News findById(Long id) {
        return newsRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("News with ID {0} not found", id)));
    }

    @Override
    public ResponseEntity<String> save(News news) {
        newsRepository.save(news);
        return ResponseEntity.ok(MessageFormat.format("News with text -  {0} save", news.getText()));
    }

    @Transactional
    @Override
    public ResponseEntity<String> update(News news) {
        Optional<News> existedNews = newsRepository.findById(news.getId());
        if (existedNews.isPresent()) {
            BeanUtils.copyNonNullProperties(news, existedNews.get());
            newsRepository.save(existedNews.get());
            return ResponseEntity.ok(MessageFormat.format("News with ID {0} updated", news.getId()));
        } else {
            return ResponseEntity.ok(MessageFormat.format("News with ID {0} not found", news.getId()));
        }
    }

    @Transactional
    @Override
    public ResponseEntity<String> deleteById(Long id) {
        Optional<News> newsRepositoryById = newsRepository.findById(id);
        if (newsRepositoryById.isEmpty()) {
            return ResponseEntity.ok(MessageFormat.format("News with ID {0} not found", id));
        } else {
            newsRepository.deleteById(id);
            return ResponseEntity.ok(MessageFormat.format("News with ID {0} deleted", id));
        }


    }

    @Override
    public List<News> filterByCategory(String category) {
        NewsFilter filter = new NewsFilter(category);
        List<News> newsList = newsRepository.findAllByCategory(filter.getCategory());
        if (newsList.size() == 0) {
            log.info("News with this category have not yet been created");
            return null;
        } else {
            return newsList;
        }
    }

    @Transactional
    @Override
    public List<News> filterByUser_id(UserDetails userDetails) {
        long userId = userRepository.findByNickname(userDetails.getUsername()).get().getId();
        int count = newsRepository.findAllByUser_id(userId).size();
        if (count == 0) {
            throw new EntityNotFoundException(MessageFormat.format("The user with ID {0} did not create the news", userId));
        }
        return newsRepository.findAllByUser_id(userId);
    }


}
