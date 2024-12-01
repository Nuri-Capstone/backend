package com.nuri.nuribackend.dto.Feedback;

import com.nuri.nuribackend.domain.Feedback.FeedbackContent;
import lombok.Data;

@Data
public class MonthlyFeedbackFromGPT {
    FeedbackContent grammar = new FeedbackContent();            // 문법에 대한 요약 피드백
    FeedbackContent vocabulary = new FeedbackContent();         // 어휘에 대한 요약 피드백
    FeedbackContent FormalInformal = new FeedbackContent();     // 경어체에 대한 요약 피드백
}
