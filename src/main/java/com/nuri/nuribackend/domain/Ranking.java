package com.nuri.nuribackend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "ranking")
public class Ranking {

    @Id
    private Integer rankingId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "msg_total_cnt", nullable = false)
    private Integer msgTotalCnt;

    @Column(name = "date", nullable = false)
    private Date date;
}
