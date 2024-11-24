package com.nuri.nuribackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuri.nuribackend.dto.ChatMessageDto;
import com.nuri.nuribackend.repository.ChatMessageRepository;
import com.nuri.nuribackend.domain.ChatMessage;
import com.nuri.nuribackend.service.ChatMessageService;
import com.nuri.nuribackend.service.S3Service;
import com.nuri.nuribackend.service.TranscribeService;
import com.nuri.nuribackend.service.GPTService;  // GPTService 추가
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.transcribe.TranscribeClient;
import software.amazon.awssdk.services.transcribe.model.TranscriptionJobStatus;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketVoiceHandler extends BinaryWebSocketHandler {
    private final ObjectMapper mapper;
    private final Set<WebSocketSession> sessions = new HashSet<>();
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageService chatMessageService;
    private final S3Service s3Service;
    private final GPTService gptService;  // GPTService
    private int newChatId;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        newChatId = generateNewChatId();
        log.info("Voice Connected: {} - Total sessions: {}", session.getId(), sessions.size());
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        ByteBuffer payload = message.getPayload();
        log.info("Received binary message from session: {} - Payload size: {}", session.getId(), payload.remaining());

        byte[] audioData = new byte[payload.remaining()];
        payload.get(audioData);

        try {
            processAudioData(session, audioData);
            session.sendMessage(new BinaryMessage(ByteBuffer.wrap("Audio received".getBytes())));
        } catch (Exception e) {
            log.error("Error processing audio data for session: {}", session.getId(), e);
            try {
                session.sendMessage(new BinaryMessage(ByteBuffer.wrap("Error processing audio data".getBytes())));
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

            String responseJson = mapper.writeValueAsString(chatMessage);
            session.sendMessage(new TextMessage(responseJson));

            ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
            System.out.println("Generated Chat ID: " + savedMessage.getChatId().toString());

            // GPT API
            ChatMessage gptMessage = gptService.handleTextGPT(result);
            gptMessage.setChatId(newChatId);
            String gptResponseJson = mapper.writeValueAsString(gptMessage);
            session.sendMessage(new TextMessage(gptResponseJson));
            chatMessageRepository.save(gptMessage);
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