package com.nuri.nuribackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.core.sync.RequestBody;

@Service
public class S3Service {

    private final S3Client s3Client;

    @Autowired
    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void uploadByteArrayToS3(String bucketName, String key, byte[] audioData, String contentType) {
        // S3에 업로드할 객체 요청 생성, Content-Type 추가
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName) // 버킷 이름
                .key(key)           // 파일 경로
                .contentType(contentType)  // 파일의 Content-Type 설정 (예: audio/mpeg)
                .build();

        // 바이트 배열을 RequestBody로 변환하여 S3에 업로드
        PutObjectResponse response = s3Client.putObject(putObjectRequest,
                RequestBody.fromBytes(audioData));

        // 업로드가 완료되면 버전 ID를 로그로 출력
        System.out.println("파일 업로드 완료, 버전: " + response.versionId());
    }


    public String getFileUrl(String bucketName, String fileName) {
        return "https://" + bucketName + ".s3." + Region.AP_NORTHEAST_2.id() + ".amazonaws.com/" + fileName;
    }




}
