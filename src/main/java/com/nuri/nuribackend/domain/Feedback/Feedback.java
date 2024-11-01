package com.nuri.nuribackend.domain.Feedback;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "feedback")
@Data
public class Feedback {

    @Id
    private String feedbackId;
    private String msgId;   // 피드백이 달린 말풍선 ID

    private FeedbackContent grammar;
    private FeedbackContent vocabulary;
    private FeedbackContent ageInGroup;
    private FeedbackContent FormalInformal;
}
