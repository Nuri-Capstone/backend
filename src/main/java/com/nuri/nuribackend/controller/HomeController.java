package com.nuri.nuribackend.controller;

import com.nuri.nuribackend.domain.Feedback.MonthlyFeedback;
import com.nuri.nuribackend.dto.Feedback.MonthlyFeedbackFromGPT;
import com.nuri.nuribackend.service.home.MonthlyFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class HomeController {

    private final MonthlyFeedbackService monthlyFeedbackService;

    @Autowired
    public HomeController(MonthlyFeedbackService monthlyFeedbackService) {
        this.monthlyFeedbackService = monthlyFeedbackService;
    }

    // 랭킹 조회 컨트롤러
    @GetMapping("/ranking")
    public String getRanking() {
        // rankingService 호출해서 데이터 받아오기
        // 받아온 데이터 모델에 담아서 프론트에 넘겨주기
        return "";
    }

    // 월별 피드백 조회 컨트롤러
    @GetMapping("/monthlyFeedback/{userId}/{year}/{month}")
    public MonthlyFeedbackFromGPT getMonthlyFeedback(@PathVariable Long userId, @PathVariable int year, @PathVariable int month) {
        return monthlyFeedbackService.getMonthlyFeedback(userId, year, month);
    }
}
