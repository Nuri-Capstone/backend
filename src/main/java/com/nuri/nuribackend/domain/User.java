package com.nuri.nuribackend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "user_img")
    private String userImg;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)       // 1명의 사용자는 여러 채팅방 생성 가능
    @JsonIgnore
    @ToString.Exclude
    private List<Chat> chatList;

    @OneToMany(mappedBy = "user")        // 1명의 사용자에 대한 ranking 데이터는 1개 존재
    private List<Ranking> ranking;
}