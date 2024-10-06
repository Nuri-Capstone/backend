package com.nuri.nuribackend.dto.User;

import com.nuri.nuribackend.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
public class UserGetResponse {
    private Long id;
    private String userId;

    @Builder
    public UserGetResponse(User user){
        this.id = user.getId();
        this.userId = user.getUserId();
    }
}
