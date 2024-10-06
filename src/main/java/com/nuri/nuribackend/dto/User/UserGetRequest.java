package com.nuri.nuribackend.dto.User;

import com.nuri.nuribackend.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserGetRequest {
    private Long id;
    private String userId;

    @Builder
    public UserGetRequest(Long id) {
        this.id = id;
    }

    public User toEntity(){
        return User.builder()
                .id(id)
                .build();
    }
}

