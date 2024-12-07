package com.nuri.nuribackend.service.home;

import com.nuri.nuribackend.domain.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OpenAiService {

    @Value("${openai.api.key}") // application.yml 파일에 저장되어 있는 key를 불러옴
    private String apiKey;

    @Value("${openai.api.url}") // application.yml 파일에 저장되어 있는 url을 불러옴
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getSummary(String category, List<String> feedback) {
        log.info("feedback: " + feedback);

        String allFeedback = String.join(", ", feedback);
        log.info("allFeedback: " + allFeedback);

        String prompt = String.format("한국어를 배우는 외국인이 받은 %s에 대해 받은 %s 피드백들을 5개의 목록 형태와 한국어로 요약해줘." +
                "출력 형태는 \"~한 점에서 피드백을 많이 받으셨어요. ~부분을 보완하시면 좋을거예요\"" +
                "\"~한 부분에서는 부족한 점이 없어요.\"" +
                "\"~한 표현보다는 ~한 표현을 사용하는 것이 더 학습하는데에 도움될거에요.\" 처럼 해줘." +
                "그리고 부가적인 내용은 출력하지 말고 \"1. 2. 3. 4. 5.\"만 보여주고 각각의 목록 사이는 줄바꿈 넣어줘.", category, allFeedback);

        // HTTP 요청헤더에 발급받은 API key 넣음
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        // Map.of는 변경할 수 없는 Map 자료구조를 반환
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o",
                "messages", List.of(
                        Map.of("role", "system", "content", "너는 한국어를 배우고 싶어하는 외국인들이 한국어를 학습할때 받은 피드백을 요약해서 앞으로의 학습 방향성을 제시해주는 사람이야."),
                        Map.of("role", "user", "content", prompt)
                ),
                "max_tokens", 300
        );

        // HttpEntity객체에 위에서 작성한 헤더와 요청바디를 담아서 구성함
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // restTemplate이라는 HTTP 클라이언트를 사용하여 gpt에게 요청을 보냄 (응답받는 타입은 Map으로 지정함)
        ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, Map.class);

        // gpt로 받은 응답에서 body부분만 추출함
        Map<String, Object> responseBody = response.getBody();
        log.info("responseBody: " + responseBody);

        if (responseBody != null && responseBody.containsKey("choices")) {
            List<?> choices = (List<?>) responseBody.get("choices");
            if (!choices.isEmpty()) {
                Map<String, Object> firstChoice = (Map<String, Object>) choices.get(0);
                if (firstChoice.containsKey("message")) {
                    Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
                    if (message.containsKey("content")) {
                        return (String) message.get("content");
                    }
                }
            }
        }
        return "";
    }

    public String getChatSummary(List<ChatMessage> messages) {

        StringBuilder sb = new StringBuilder();
        for (int m = 0; m < messages.size(); m++) {
            sb.append(messages.get(m)).append(", ");
        }
        String conversation = sb.toString();
        log.info("채팅방 메시지: " + conversation);

        String prompt = String.format("%s는 대화 내용이야. 대화 내용을 분석해서 대화를 20글자 이내로 요약해줘." +
                "요약의 마지막은 \"이야기\"라는 단어로 끝맺음 지어줘."
                , conversation);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o",
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                ),
                "max_tokens", 60
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, Map.class);
        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");

        Map<String, String> message = (Map<String, String>) choices.get(0).get("message");

        return message.get("content");
    }
}
