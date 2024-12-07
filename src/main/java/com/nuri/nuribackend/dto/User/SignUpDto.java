package com.nuri.nuribackend.dto.User;

import com.nuri.nuribackend.domain.User;
import lombok.Data;

import java.util.Base64;

@Data
public class SignUpDto {
    private String email;
    private String name;
    private String password;
//    private String userImg;

    public User toEntity(String encordedPassword){
        return User.builder()
                .email(email)
                .name(name)
                .password(encordedPassword)
//                .userImg(userImg)
                .build();
    }
};