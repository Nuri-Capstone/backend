package com.nuri.nuribackend.controller;

import com.nuri.nuribackend.dto.User.JwtTokenDto;
import com.nuri.nuribackend.dto.User.SignInDto;
import com.nuri.nuribackend.dto.User.SignUpDto;
import com.nuri.nuribackend.dto.User.UserDto;
import com.nuri.nuribackend.service.S3Service;
import com.nuri.nuribackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final S3Service s3Service;

    @Autowired
    public UserController(S3Service s3Service, UserService userService) {
        this.s3Service = s3Service;
        this.userService = userService;
    }

    // Method: POST URL: http://localhost:8080/users
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> createUser(@RequestPart("email") String email,
                                              @RequestPart("name") String name,
                                              @RequestPart("password") String password) throws IOException {
        SignUpDto signUpDto = new SignUpDto();
//        String key = "user-images/" + name;
//        String s3Url = s3Service.uploadProfile("nuri-s3", key, userImg.getInputStream(), userImg.getContentType());

        // s3에 프로필 이미지 업로드
        signUpDto.setEmail(email);
        signUpDto.setName(name);
        signUpDto.setPassword(password);
//        signUpDto.setUserImg(s3Url);

        UserDto savedUser = userService.addUser(signUpDto); // 사용자 추가 처리
        return ResponseEntity.ok(savedUser); // 결과 반환
    }

    // Method: POST URL: http://localhost:8080/users/login
    @PostMapping("/login")
    public JwtTokenDto signIn(@RequestBody SignInDto signInDto) {
        String email = signInDto.getEmail();
        String password = signInDto.getPassword();

        JwtTokenDto jwtToken = userService.signIn(email, password);
        return jwtToken;
    }


    // Method: GET URL: http://localhost:8080/users/all
    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getUserList(){
        List<UserDto> userList = userService.getAllUsers();
        return ResponseEntity.ok(userList);
    }

    // Method: GET URL: http://localhost:8080/users?userId=1
    @GetMapping
    public ResponseEntity<UserDto> getUserByParams(@RequestParam Long userId){
        UserDto user = userService.getUserByUserId(userId);
        return ResponseEntity.ok(user);
    }

    // Method: GET URL: http://localhost:8080/users/1
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserByPath(@PathVariable Long userId){
        UserDto user = userService.getUserByUserId(userId);
        return ResponseEntity.ok(user);
    }

}
