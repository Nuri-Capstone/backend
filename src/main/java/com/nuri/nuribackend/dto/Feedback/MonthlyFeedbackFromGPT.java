package com.nuri.nuribackend.dto.Feedback;

import com.nuri.nuribackend.domain.Feedback.FeedbackContent;
import lombok.Data;

@Data
public class MonthlyFeedbackFromGPT {
    private FeedbackContent grammar = new FeedbackContent();            // 문법에 대한 요약 피드백
    private FeedbackContent vocabulary = new FeedbackContent();         // 어휘에 대한 요약 피드백
    private FeedbackContent ageInGroup = new FeedbackContent();         // 대화 연령의 적합정도에 대한 요약 피드백
    private FeedbackContent FormalInformal = new FeedbackContent();     // 경어체에 대한 요약 피드백
}
