package com.nuri.nuribackend.domain.Feedback;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "monthly_feedback")
@Data
public class MonthlyFeedback {

    @Id
    private String id;                  // 월별 피드백 ID

    private Long userId;                      // 사용자 ID

    private FeedbackContent grammar = new FeedbackContent();            // 문법에 대한 요약 피드백
    private FeedbackContent vocabulary = new FeedbackContent();         // 어휘에 대한 요약 피드백
    private FeedbackContent FormalInformal = new FeedbackContent();     // 경어체에 대한 요약 피드백

    private Date date;                          // 피드백 생성한 월(month)
}
