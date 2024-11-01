package com.nuri.nuribackend.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "ranking")
public class Ranking {

    @Id
    private Integer rankingId;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "msg_total_cnt", nullable = false)
    private Integer msgTotalCnt;

    @Column(name = "date", nullable = false)
    private Date date;
}
