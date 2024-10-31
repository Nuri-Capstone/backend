package com.nuri.nuribackend.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "msg_id", nullable = false)
    private Integer msgId;

    @Column(name = "grammar", nullable = false)
    private String grammar;

    @Column(name = "vocabulary", nullable = false)
    private String vocabulary;

    @Column(name = "age_in_group", nullable = false)
    private String ageInGroup;

    @Column(name = "formal_informal", nullable = false)
    private String formalInformal;
}
