package com.demo.user.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.demo.user.dto.UserRequest;
import com.demo.user.entity.Users;
import com.demo.user.exception.BusinessException;
import com.demo.user.inputvalidator.ValidateUser;
import com.demo.user.mapper.UserMapper;
import com.demo.user.repository.UserRepository;
import com.demo.user.security.jwt.JwtUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final RedisService redisService;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidateUser validateUser;

    public AuthService(RedisService redisService, JwtUtils jwtUtils, UserMapper userMapper,
            UserRepository userRepository, PasswordEncoder passwordEncoder, ValidateUser validateUser) {
        this.redisService = redisService;
        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.validateUser = validateUser;
    }

    public Object refreshToken(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = extractRefreshToken(request);
        var userId = redisService.validateRefreshToken(refreshToken);
        refreshToken = "refresh_token:" + refreshToken;
        redisService.delete(refreshToken);
        var accessToken = jwtUtils.generateAccessToken(userId, "USER");
        var newRefreshToken = jwtUtils.generateRefreshToken(userId, "USER");
        redisService.storeRefreshToken(newRefreshToken, userId);

        Cookie cookie = new Cookie("refreshToken", newRefreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);

        return Map.of("accessToken", accessToken);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response, String accessToken) {

        String refreshToken = extractRefreshToken(request);
        redisService.delete("refresh_token:" + refreshToken);
        redisService.blackListAccessToken(accessToken);

        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        log.info("user logged out successfully");

    }

    public void register(UserRequest request) {

        var user = userMapper.toEntity(request);

        user.setRoleId(userRepository.getRoleIdByName("USER"));

        user.setCreatedAt(LocalDateTime.now());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        validateUser.checkEmailAlreadyExists(user);

        var res = userRepository.save(user);

        if (res == null) {
            throw new BusinessException("User not registered");
        }

        log.info("user registered successfully | name:{} | email:{}", user.getName(), user.getEmail());
    }

    public Object login(UserRequest userRequest, HttpServletResponse response) {

        Users userObj = userMapper.toEntity(userRequest);

        Users user = userRepository.getUserByEmail(userObj.getEmail());

        validateUser.checkUserExists(user);

        validateUser.checkPassword(user);

        var accessToken = jwtUtils.generateAccessToken(user.getId().toString(), user.getRoleId().toString());
        var refreshToken = jwtUtils.generateRefreshToken(user.getId().toString(), user.getRoleId().toString());

        redisService.storeRefreshToken(refreshToken, user.getId().toString());

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);

        log.info("user logged in successfully | name:{} | email:{}", user.getName(), user.getEmail());

        return Map.of("accessToken", accessToken);
    }

    public String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    return cookie.getValue();
                }
            }
        }
        throw new RuntimeException("No refresh token found");
    }

}
