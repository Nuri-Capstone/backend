package com.nuri.nuribackend.service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.transcribe.TranscribeClient;
import software.amazon.awssdk.services.transcribe.model.Media;
import software.amazon.awssdk.services.transcribe.model.StartTranscriptionJobRequest;
import software.amazon.awssdk.services.transcribe.model.StartTranscriptionJobResponse;
import software.amazon.awssdk.services.transcribe.model.TranscriptionJobStatus;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class TranscribeService {

    private final TranscribeClient transcribeClient;

    public TranscribeService(TranscribeClient transcribeClient) {
        this.transcribeClient = transcribeClient;
    }

    public String startTranscriptionJob(String bucketName, String fileName, String jobName) {
        // S3 URL 형식으로 변환
        String s3Uri = "s3://" + bucketName + "/" + fileName;
        System.out.println(s3Uri);

        // Media 객체 생성
        Media media = Media.builder()
                .mediaFileUri(s3Uri)
                .build();

        // Transcription 작업 요청 설정
        StartTranscriptionJobRequest request = StartTranscriptionJobRequest.builder()
                .transcriptionJobName(jobName)
                .media(media)
                .mediaFormat("mp3")
                .languageCode("ko-KR")
                .build();

        // Transcription 작업 시작
        StartTranscriptionJobResponse response = transcribeClient.startTranscriptionJob(request);
        return response.transcriptionJob().transcriptionJobName();
    }

    public TranscriptionJobStatus checkTranscriptionJobStatus(String jobName) {
        var job = transcribeClient.getTranscriptionJob(r -> r.transcriptionJobName(jobName)).transcriptionJob();
        return job.transcriptionJobStatus();
    }

    public String getTranscriptionJobResult(String jobName) throws IOException {
        // Transcription Job의 결과 URL 가져오기
        var job = transcribeClient.getTranscriptionJob(r -> r.transcriptionJobName(jobName)).transcriptionJob();
        String transcriptFileUri = job.transcript().transcriptFileUri();

        // 결과 JSON URL에서 데이터 읽기
        URL url = new URL(transcriptFileUri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // JSON 데이터 읽기
        Scanner scanner = new Scanner(connection.getInputStream());
        StringBuilder jsonResponse = new StringBuilder();
        while (scanner.hasNext()) {
            jsonResponse.append(scanner.nextLine());
        }
        scanner.close();

        // JSON 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(jsonResponse.toString());

        // "transcript" 필드에서 텍스트 추출
        String transcript = root.path("results").path("transcripts").get(0).path("transcript").asText();
        System.out.println("Transcript: " + transcript);

        return transcript;
    }
}
