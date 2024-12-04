package com.nuri.nuribackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuri.nuribackend.dto.ChatMessageDto;
import com.nuri.nuribackend.dto.GPT.FeedbackMessage;
import com.nuri.nuribackend.repository.ChatMessageRepository;
import com.nuri.nuribackend.domain.ChatMessage;
import com.nuri.nuribackend.domain.Feedback.Feedback;
import com.nuri.nuribackend.repository.FeedbackRepository;
import com.nuri.nuribackend.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.transcribe.TranscribeClient;
import software.amazon.awssdk.services.transcribe.model.TranscriptionJobStatus;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketVoiceHandler extends AbstractWebSocketHandler {
    private final ObjectMapper mapper;
    private final Set<WebSocketSession> sessions = new HashSet<>();
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageService chatMessageService;
    private final FeedbackRepository feedbackRepository;
    private final S3Service s3Service;
    private final GPTService gptService;  // GPTService
    private final PollyService pollyService;
    private int newChatId;
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        newChatId = generateNewChatId();
        log.info("Voice Connected: {} - Total sessions: {}", session.getId(), sessions.size());
    }


    // handleTextMessage for GPT feedback
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {

        System.out.println("Text Message Received: " + message.getPayload());

        try {
            String payload = message.getPayload();
            ObjectMapper objectMapper = new ObjectMapper();

            FeedbackMessage feedbackMessage = objectMapper.readValue(payload, FeedbackMessage.class);

            String msgText = feedbackMessage.getMsgText();
            String msgId = feedbackMessage.getMsgId();
            String feedbackType = feedbackMessage.getFeedbackType();
            System.out.println("msgId: " + msgId);

            // GPT 피드백 생성
            Feedback gptFeedback = gptService.handleFeedbackGPT(msgId, msgText, feedbackType);

            // 요청한 피드백 유형만 포함된 JSON 생성
            Object filteredFeedback = null;
            switch (feedbackType.toLowerCase()) {
                case "grammar" -> filteredFeedback = gptFeedback.getGrammar();
                case "vocabulary" -> filteredFeedback = gptFeedback.getVocabulary();
                case "formal / informal" -> filteredFeedback = gptFeedback.getFormalInformal();
                default -> throw new IllegalArgumentException("Invalid feedback type: " + feedbackType);
            }

            // 필터링된 데이터만 JSON으로 변환 후 전송
            String gptFeedbackResponse = objectMapper.writeValueAsString(filteredFeedback);
            session.sendMessage(new TextMessage(gptFeedbackResponse));
            System.out.println("전송된 피드백 데이터: " + gptFeedbackResponse);

            feedbackRepository.save(gptFeedback);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        ByteBuffer payload = message.getPayload();
        log.info("Received binary message from session: {} - Payload size: {}", session.getId(), payload.remaining());

        byte[] audioData = new byte[payload.remaining()];
        payload.get(audioData);

        try {
            processAudioData(session, audioData);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Audio received");
            String jsonResponse = objectMapper.writeValueAsString(response);
            session.sendMessage(new TextMessage(jsonResponse));
        } catch (Exception e) {
            log.error("Error processing audio data for session: {}", session.getId(), e);
            try {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Error processing audio data");
                String jsonResponse = objectMapper.writeValueAsString(response);
                session.sendMessage(new TextMessage(jsonResponse));
            } catch (IOException ex) {
                log.error("Error sending error message to client for session: {}", session.getId(), ex);
            }
        }
    }

    private void processAudioData(WebSocketSession session, byte[] audioData) {
        log.info("Processing audio data from session: {} - Size: {}", session.getId(), audioData.length);

        String contentType = "audio/mpeg";
        String fileName = "voice/" + "audio-" + UUID.randomUUID().toString() + ".mp3";

        new Thread(() -> {
            try {
                log.info("Uploading audio to S3 - FileName: {}", fileName);
                s3Service.uploadByteArrayToS3("nuri-s3", fileName, audioData, contentType);
                log.info("Successfully uploaded audio to S3 - FileName: {}", fileName);
                transcriptAudioData(session, fileName);
            } catch (Exception e) {
                log.error("Error uploading audio data to S3 for session: {} - FileName: {}", session.getId(), fileName, e);
            }
        }).start();
    }

    protected void transcriptAudioData(WebSocketSession session, String url) throws InterruptedException, IOException {
        TranscribeClient transcribeClient = TranscribeClient.builder()
                .region(Region.AP_NORTHEAST_2) // 리전 설정
                .build();
        TranscribeService transcribeService = new TranscribeService(transcribeClient);
        String bucketName = "nuri-s3";
        String jobName = "my-transcription-job-" + UUID.randomUUID();

        String transcriptionJobName = transcribeService.startTranscriptionJob(bucketName, url, jobName);
        System.out.println("Started Transcription Job: " + transcriptionJobName);


        TranscriptionJobStatus status;
        do {
            status = transcribeService.checkTranscriptionJobStatus(transcriptionJobName);
            System.out.println("Transcription Job Status: " + status);
            Thread.sleep(5000); // 5초 대기
        } while (status == TranscriptionJobStatus.IN_PROGRESS);

        // 작업 완료 시 결과 URL 가져오기
        if (status == TranscriptionJobStatus.COMPLETED) {
            String result = transcribeService.getTranscriptionJobResult(transcriptionJobName);
            System.out.println("Transcription Result URL: " + result);
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setMsgType("user");
            chatMessage.setMsgText(result);
            chatMessage.setMsgSound(url);
            chatMessage.setChatId(newChatId);
            chatMessage.setTimeStamp(LocalDateTime.now());

            ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

            String msgId = savedMessage.getId(); // 저장된 msgId를 가져옴
            chatMessage.setId(msgId); // 클라이언트에 msgId도 전송 (후에 피드백 요청시 사용)

            String responseJson = mapper.writeValueAsString(chatMessage);
            session.sendMessage(new TextMessage(responseJson));

            System.out.println("Generated Chat ID: " + savedMessage.getChatId().toString());

            // GPT API
            ChatMessage gptMessage = gptService.handleTextGPT(result);
            gptMessage.setChatId(newChatId);
            String gptResponseJson = mapper.writeValueAsString(gptMessage);
            session.sendMessage(new TextMessage(gptResponseJson));
            chatMessageRepository.save(gptMessage);

            pollyService.initializePollyClient();
            String audioFileName = "polly/" + "speech-" + UUID.randomUUID() + ".mp3";
            try {
                byte[] audioData = pollyService.synthesizeSpeechToByteArray(gptMessage.getMsgText());
                s3Service.uploadByteArrayToS3(bucketName, audioFileName, audioData, "audio/mpeg");

                // 클라이언트로 음성 URL 전달
                String audioUrl = s3Service.getFileUrl(bucketName, audioFileName);

                Map<String, String> response = new HashMap<>();
                response.put("audioUrl", audioUrl); // S3 URL 추가
                response.put("message", "Audio has been generated successfully");
                String jsonResponse = objectMapper.writeValueAsString(response);

                session.sendMessage(new TextMessage(jsonResponse));
            } catch (Exception e) {
                log.error("Error generating or sending Polly audio for session: {}", session.getId(), e);
            }

        } else {
            System.out.println("Transcription Job Failed.");
        }

        transcribeClient.close();
    }




    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        log.info("Voice Disconnected: {} - CloseStatus: {} - Remaining sessions: {}", session.getId(), status, sessions.size());
    }

    private Integer generateNewChatId() {
        return (int) (UUID.randomUUID().getLeastSignificantBits() & 0x7FFFFFFF);
    }

}
