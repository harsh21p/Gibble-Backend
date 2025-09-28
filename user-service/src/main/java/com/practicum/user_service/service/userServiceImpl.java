package com.practicum.user_service.service;

import com.practicum.user_service.entities.User;
import com.practicum.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class userServiceImpl implements userService{

    @Autowired
    UserRepository userRepository;

    @Override
    public String getUserId(String email) {
        User user = new User();
        user = userRepository.findByEmail(email);
        return user.getId();
    }
}
