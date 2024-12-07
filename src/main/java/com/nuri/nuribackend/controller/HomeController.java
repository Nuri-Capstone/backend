package com.nuri.nuribackend.controller;

import com.nuri.nuribackend.domain.Feedback.MonthlyFeedback;
import com.nuri.nuribackend.service.RankingService;
import com.nuri.nuribackend.service.home.MonthlyFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/home")
public class HomeController {

    private final MonthlyFeedbackService monthlyFeedbackService;
    private final RankingService rankingService;

    @Autowired
    public HomeController(MonthlyFeedbackService monthlyFeedbackService, RankingService rankingService) {
        this.monthlyFeedbackService = monthlyFeedbackService;
        this.rankingService = rankingService;
    }

    @GetMapping("/ranking/allUsers")
    public ResponseEntity<Integer> getAllUserCount() {
        int allUser = rankingService.getAllUser();
        return ResponseEntity.ok(allUser);
    }

    // 특정 사용자의 순위 반환
    @GetMapping("/ranking/{userId}")
    public ResponseEntity<Integer> getUserRanking(@PathVariable Long userId) {
        int userRanking = rankingService.getUserRanking(userId);
        return ResponseEntity.ok(userRanking);
    }

    // 특정 사용자의 월별 대화 수 조회 컨트롤러
    @GetMapping("/graph/{userId}")
    public Map<LocalDate, Integer> getGraph(@PathVariable Long userId) {
        return rankingService.getGraph(userId);
    }

    // 월별 피드백 조회 컨트롤러
    @GetMapping("/monthlyFeedback/{userId}/{year}/{month}")
    public MonthlyFeedback getMonthlyFeedback(@PathVariable Long userId, @PathVariable int year, @PathVariable int month) {
        return monthlyFeedbackService.getMonthlyFeedback(userId, year, month);
    }
}
