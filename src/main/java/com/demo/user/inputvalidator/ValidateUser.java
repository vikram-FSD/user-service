package com.demo.user.inputvalidator;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.demo.user.entity.Users;
import com.demo.user.exception.BusinessException;
import com.demo.user.repository.UserRepository;

@Component
public class ValidateUser {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ValidateUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void checkEmailAlreadyExists(Users user) {

        if (userRepository.emailExists(user.getEmail())) {
            throw new BusinessException("Email already exists");
        }

    }

    public void checkUserExists(Users user) {
        if (!userRepository.emailExists(user.getEmail())) {
            throw new BusinessException("User not found");
        }
    }

    public void checkPassword(Users user) {
        if (passwordEncoder.matches(user.getPassword(), user.getPassword())) {
            throw new BusinessException("Invalid password");
        }
    }

}
