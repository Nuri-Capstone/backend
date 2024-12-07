package com.nuri.nuribackend.dto.Feedback;
import com.nuri.nuribackend.domain.Ranking;
import com.nuri.nuribackend.domain.User;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RankingDto {
    private Integer rankingId;
    private User user;
    private Integer msgTotalCnt;
    private Date date;
    static public RankingDto toDto(Ranking ranking){
        return RankingDto.builder()
                .rankingId(ranking.getRankingId())
                .user(ranking.getUser())
                .msgTotalCnt(ranking.getMsgTotalCnt())
                .date(ranking.getDate())
                .build();
    }

    public Ranking toEntity(){
        return Ranking.builder()
                .rankingId(rankingId)
                .user(user)
                .msgTotalCnt(msgTotalCnt)
                .date(date)
                .build();
    }

}
