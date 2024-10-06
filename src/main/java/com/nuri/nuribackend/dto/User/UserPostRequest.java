package com.nuri.nuribackend.dto.User;

import com.nuri.nuribackend.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
public class UserPostRequest {
    private String userId;
    private String password;

    @Builder
    public UserPostRequest(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public User toEntity(){
        return User.builder()
                .userId(userId)
                .password(password)
                .build();
    }
}

