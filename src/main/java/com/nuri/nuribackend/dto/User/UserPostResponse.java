package com.nuri.nuribackend.dto.User;

import com.nuri.nuribackend.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
public class UserPostResponse {
    private Long id;
    private String userId;
    private String password;

    @Builder
    public UserPostResponse(User user){
        this.id = user.getId();
        this.userId = user.getUserId();
        this.password = user.getPassword();
    }
}
