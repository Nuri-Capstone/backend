package com.nuri.nuribackend.dto.GPT;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackMessage {
    private String msgText;       // 메시지 내용
    private String feedbackType;  // 피드백 타입 (grammar, vocabulary, formalInformal)
    private String msgId;         // 메시지 ID (피드백 대상이 될 말풍선의 ID)
}
