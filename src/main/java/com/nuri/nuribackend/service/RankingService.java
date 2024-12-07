package com.nuri.nuribackend.service;

import com.nuri.nuribackend.domain.Chat;
import com.nuri.nuribackend.domain.Ranking;
import com.nuri.nuribackend.dto.ChatDto;
import com.nuri.nuribackend.dto.Feedback.RankingDto;
import com.nuri.nuribackend.exception.CustomException;
import com.nuri.nuribackend.repository.ChatRepository;
import com.nuri.nuribackend.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RankingService {
    private final RankingRepository rankingRepository;
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
}