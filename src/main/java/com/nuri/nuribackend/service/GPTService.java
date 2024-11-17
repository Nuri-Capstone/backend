package com.nuri.nuribackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuri.nuribackend.domain.ChatMessage;
import com.nuri.nuribackend.dto.GPT.GPTResponse;
import com.nuri.nuribackend.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class GPTService {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final ChatMessageRepository chatMessageRepository;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.model}")
    private String model;

    @Value("${openai.api.key}")
    private String apiKey;

    public ChatMessage handleTextGPT(String result) {
        // 사용자 메시지 설정
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", result);
        List<Map<String, String>> messages = List.of(userMessage);

        // GPT 요청 생성
        Map<String, Object> gptRequest = Map.of(
                "model", model,
                "messages", messages,
                "max_tokens", 256,
                "temperature", 0.7,
                "top_p", 0.7
        );

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(gptRequest, headers);

        try {

            // GPT API 호출
            GPTResponse gptResponse = restTemplate.postForObject(apiUrl, requestEntity, GPTResponse.class);
            System.out.println("지피티 대답: " + gptResponse);

            // GPT 응답 처리
            if (gptResponse != null && gptResponse.getChoices() != null && !gptResponse.getChoices().isEmpty()) {

                String gptReply = gptResponse.getChoices().get(0).getMessage().getContent();

                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setMsgSound(null); // url
                chatMessage.setMsgText(gptReply);
                chatMessage.setMsgType("gpt");
                chatMessage.setTimeStamp(LocalDateTime.now());

                return chatMessage;

            } else {
                log.error("GPT API에서 응답이 없습니다.");
                return null;
            }
            } catch (Exception e) {
                log.error("GPT API 호출 중 오류가 발생했습니다.", e);
                return null;
            }

    }
}
