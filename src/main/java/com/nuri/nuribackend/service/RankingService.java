package com.nuri.nuribackend.service;

import com.nuri.nuribackend.domain.Chat;
import com.nuri.nuribackend.domain.Ranking;
import com.nuri.nuribackend.dto.ChatDto;
import com.nuri.nuribackend.dto.Feedback.RankingDto;
import com.nuri.nuribackend.exception.CustomException;
import com.nuri.nuribackend.repository.ChatRepository;
import com.nuri.nuribackend.repository.RankingRepository;
import com.nuri.nuribackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RankingService {
    private final RankingRepository rankingRepository;
    private final UserRepository userRepository;

    @Transactional
    public RankingDto addRanking(RankingDto rankingDto) {
        try {
            Ranking ranking = rankingDto.toEntity();
            Ranking savedRanking = rankingRepository.save(ranking);
            RankingDto response = RankingDto.toDto(savedRanking);
            return response;
        } catch (CustomException ex){
            log.error("Error adding ranking: {}", ex.getMessage());
            throw new CustomException(ex.getErrorCode(), ex.getMessage());
        }
    }

    public int getAllUser() {
        List<Long> userIds = userRepository.findAllUserIds();
        int allUser = userIds.size();

        return allUser;
    }

    public int getUserRanking(Long userId) {
        // Long: userId, Integer: msgCnt
        List<Long> userIds = userRepository.findAllUserIds();

        List<Object[]> results = rankingRepository.getMsgCnt(userIds);
        // 내림차순 정렬
        results.sort((o1, o2) -> Long.compare((Long) o2[1], (Long) o1[1]));
        // rank를 Integer(msgCnt)를 기준으로 내림차순 정렬

        // key(userID)를 조회함
        int userRanking = -1; // 순위 초기값
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i)[0].equals(userId)) {
                userRanking = i + 1; // 순위는 인덱스 + 1
                break;
            }
        }
        return userRanking;
    }
}