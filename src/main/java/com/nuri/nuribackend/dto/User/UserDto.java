package com.nuri.nuribackend.dto.User;

import com.nuri.nuribackend.domain.User;
import lombok.*;


@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private String userImg;


    static public UserDto toDto(User user){
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .userImg(user.getUserImg())
                .build();
    }

    public User toEntity(){
        return User.builder()
                .id(id)
                .email(email)
                .name(name)
                .userImg(userImg)
                .build();
    }

}

