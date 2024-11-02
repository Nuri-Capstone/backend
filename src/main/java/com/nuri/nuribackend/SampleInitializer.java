package com.nuri.nuribackend;

import com.nuri.nuribackend.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// 데이터 저장 확인용입니다!

@Component
public class SampleInitializer implements CommandLineRunner {

    @Autowired
    private SampleService sampleService;

    @Override
    public void run(String... args) throws Exception {
        sampleService.saveUser();
        sampleService.saveChat();
        sampleService.saveChatMessage();
        sampleService.saveFeedbackSample();
        sampleService.saveMonthlyFeedbackSample();
    }
}