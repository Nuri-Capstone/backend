package com.nuri.nuribackend.controller;

import com.nuri.nuribackend.dto.User.UserGetRequest;
import com.nuri.nuribackend.dto.User.UserGetResponse;
import com.nuri.nuribackend.dto.User.UserPostRequest;
import com.nuri.nuribackend.dto.User.UserPostResponse;
import com.nuri.nuribackend.dto.UserDto;
import com.nuri.nuribackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Method: POST URL: http://localhost:8080/users
    @PostMapping
    public ResponseEntity<UserPostResponse> createUser(@RequestBody UserPostRequest UserPostRequest) {
        UserPostResponse savedUser = userService.addUser(UserPostRequest); // 사용자 추가 처리
        return ResponseEntity.ok(savedUser); // 결과 반환
    }

    // Method: GET URL: http://localhost:8080/users/all
    @GetMapping("/all")
    public ResponseEntity<List<UserGetResponse>> getUserList(){
        List<UserGetResponse> userList = userService.getAllUsers();
        return ResponseEntity.ok(userList);
    }

    // Method: GET URL: http://localhost:8080/users?userId=1
    @GetMapping
    public ResponseEntity<UserGetResponse> getUserByParams(@RequestParam Long userId){
        UserGetResponse user = userService.getUserByUserId(userId);
        return ResponseEntity.ok(user);
    }

    // Method: GET URL: http://localhost:8080/users/1
    @GetMapping("/{userId}")
    public ResponseEntity<UserGetResponse> getUserByPath(@PathVariable Long userId){
        UserGetResponse user = userService.getUserByUserId(userId);
        return ResponseEntity.ok(user);
    }

}
