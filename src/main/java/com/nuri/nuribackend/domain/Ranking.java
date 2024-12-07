package com.nuri.nuribackend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ranking")
public class Ranking {

    @Id
    @Column(name = "ranking_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rankingId;

    @ManyToOne
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "msg_total_cnt", nullable = false)
    private Integer msgTotalCnt;

    @Column(name = "date", nullable = false)
    private Date date;
}
