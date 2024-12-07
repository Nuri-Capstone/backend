package com.nuri.nuribackend.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private Long userId;
    private String password;
}

