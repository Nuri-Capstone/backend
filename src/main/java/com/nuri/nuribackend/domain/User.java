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

    @Column(name = "user_id")
    private String userId;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "password", length = 50)
    private String password;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "age")
    private Integer age;

    @Lob        // user_img 필드를 BLOB 타입으로 저장하기 위해 사용함
    @Column(name = "user_img")
    private byte[] userImg;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)       // 1명의 사용자는 여러 채팅방 생성 가능
    @JsonIgnore
    @ToString.Exclude
    private List<Chat> chatList;

    @OneToOne(mappedBy = "user")        // 1명의 사용자에 대한 ranking 데이터는 1개 존재
    private Ranking ranking;
}