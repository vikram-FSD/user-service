package com.demo.user.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.demo.user.dto.PagedResponse;
import com.demo.user.dto.UserResponse;
import com.demo.user.entity.Users;
import com.demo.user.mapper.UserMapper;
import com.demo.user.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public PagedResponse<UserResponse> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Users> userPage = userRepository.findAll(pageable);
        List<UserResponse> content = userPage.getContent().stream().map(userMapper::toResponse).toList();
        return new PagedResponse<>(content, page, userPage.getTotalElements(), userPage.getTotalPages());
    }

}
