package com.nuri.nuribackend.service;

import com.nuri.nuribackend.domain.User;
import com.nuri.nuribackend.dto.User.UserGetResponse;
import com.nuri.nuribackend.dto.User.UserPostRequest;
import com.nuri.nuribackend.dto.User.UserPostResponse;
import com.nuri.nuribackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository; // 주입
    }

    @Transactional
    public List<UserGetResponse> getAllUsers() {
        List<User> userList = userRepository.findAll();
        List<UserGetResponse> dtoList = new ArrayList<>(userList.size());

        for(User user : userList){
            UserGetResponse dto = new UserGetResponse(user);
            dtoList.add(dto);
        }

        return List.copyOf(dtoList);
    }

    @Transactional
    public UserPostResponse addUser(UserPostRequest userDto) {
        User user = userDto.toEntity();
        User savedUser = userRepository.save(user);
        UserPostResponse response = new UserPostResponse(savedUser);
        return response;
    }

    @Transactional
    public UserGetResponse getUserByUserId(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 유저가 없습니다"));
        return new UserGetResponse(user);
    }
}
