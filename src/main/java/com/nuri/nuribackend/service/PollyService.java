package com.nuri.nuribackend.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.polly.PollyClient;
import software.amazon.awssdk.services.polly.model.SynthesizeSpeechRequest;
import software.amazon.awssdk.services.polly.model.SynthesizeSpeechResponse;
import software.amazon.awssdk.services.polly.model.VoiceId;

@Service
public class PollyService {

    private PollyClient pollyClient;
    private static final Logger log = LoggerFactory.getLogger(PollyService.class);

    @Value("${aws.access-key-id}")
    private String accessKey;

    @Value("${aws.secret-access-key}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;


    public void initializePollyClient() {
        this.pollyClient = PollyClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .region(Region.of(region))
                .build();
    }

    public byte[] synthesizeSpeechToByteArray(String text) {
        try {
            SynthesizeSpeechRequest request = SynthesizeSpeechRequest.builder()
                    .text(text)
                    .voiceId(VoiceId.SEOYEON) // 원하는 음성 선택
                    .outputFormat("mp3")
                    .build();

            ResponseInputStream<SynthesizeSpeechResponse> response = pollyClient.synthesizeSpeech(request);

            // 바이트 배열로 변환
            return response.readAllBytes();
        } catch (Exception e) {
            log.error("Error synthesizing speech: {}", e.getMessage());
            throw new RuntimeException("Failed to synthesize speech", e);
        }
    }

}
