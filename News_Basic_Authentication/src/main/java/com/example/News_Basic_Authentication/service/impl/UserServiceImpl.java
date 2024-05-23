package com.example.News_Basic_Authentication.service.impl;


import com.example.News_Basic_Authentication.model.user.Role;
import com.example.News_Basic_Authentication.model.user.RoleType;
import com.example.News_Basic_Authentication.model.user.User;
import com.example.News_Basic_Authentication.repository.UserRepository;
import com.example.News_Basic_Authentication.service.UserService;
import com.example.News_Basic_Authentication.utils.BeanUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private boolean checkUser = false;


    @Override
    public List<User> findAll(int pageNumber, int pageSize) {
        return userRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
    }

    @Transactional
    @Override
    public User findById(Long id, UserDetails userDetails) {
        if (checkUser) {
            checkUser = false;
            id = userRepository.findByNickname(userDetails.getUsername()).get().getId();
            return userRepository.findById(id).get();
        } else return this.findById(id);
    }

    @Override
    public User findById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            log.info(MessageFormat.format("User ID {0} not found", id));
            return null;
        }
    }

    @Override
    public ResponseEntity<String> create(User user, RoleType roleType) {
        Role role = Role.from(roleType);
        user.setRoles(Collections.singletonList(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        role.setUser(user);
        userRepository.saveAndFlush(user);
        return ResponseEntity.ok(MessageFormat.format("User with Nickname   {0} save", user.getNickname()));
    }

    @Override
    public ResponseEntity<String> update(User user) {
        Optional<User> existedUser = userRepository.findById(user.getId());
        if (existedUser.isPresent()) {
            BeanUtils.copyNonNullProperties(user, existedUser.get());
            userRepository.save(existedUser.get());
            return ResponseEntity.ok(MessageFormat.format("User with ID {0} updated", user.getId()));
        } else {
            return ResponseEntity.ok(MessageFormat.format("User with ID {0} not found", user.getId()));
        }
    }

    @Override
    public void dellAll() {
        userRepository.deleteAll();
    }

    @Transactional
    @Override
    public ResponseEntity<String> deleteById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.ok(MessageFormat.format("User with ID {0} not found", id));
        } else {
            userRepository.deleteById(id);
            return ResponseEntity.ok(MessageFormat.format("User with ID {0}  deleted", id));
        }


    }

    @Override
    public User findByUserNickname(String nickname) {
        return userRepository.findByNickname(nickname).orElseThrow(() -> new RuntimeException("Username not found!"));
    }

    @Override
    public void setCheck(Boolean aBoolean) {
        checkUser = aBoolean;
    }


}
//.orElseThrow(() ->
//                new EntityNo
