package com.nuri.nuribackend.service;

import com.nuri.nuribackend.domain.ChatMessage;
import com.nuri.nuribackend.domain.Feedback.Feedback;
import com.nuri.nuribackend.domain.Feedback.FeedbackContent;
import com.nuri.nuribackend.domain.Feedback.MonthlyFeedback;
import com.nuri.nuribackend.repository.ChatRepository;
import com.nuri.nuribackend.repository.FeedbackRepository;
import com.nuri.nuribackend.repository.MonthlyFeedbackRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

// 데이터 저장 확인용 서비스임! (실제 서비스 X)

@Service
public class SampleService {

    private final FeedbackRepository feedbackRepository;
    private final MonthlyFeedbackRepository monthlyFeedbackRepository;
    private final ChatRepository chatRepository;

    public SampleService(FeedbackRepository feedbackRepository,
                         MonthlyFeedbackRepository monthlyFeedbackRepository,
                         ChatRepository chatRepository) {
        this.feedbackRepository = feedbackRepository;
        this.monthlyFeedbackRepository = monthlyFeedbackRepository;
        this.chatRepository = chatRepository;
    }

    public void saveFeedbackSample() {
        Feedback feedback = new Feedback();
        feedback.setFeedbackId("1");
        feedback.setMsgId("msg123");

        FeedbackContent grammar = new FeedbackContent();
        grammar.setContent("Grammar feedback");
        feedback.setGrammar(grammar);

        FeedbackContent vocabulary = new FeedbackContent();
        vocabulary.setContent("Vocabulary feedback");
        feedback.setVocabulary(vocabulary);

        FeedbackContent ageInGroup = new FeedbackContent();
        ageInGroup.setContent("Age group feedback");
        feedback.setAgeInGroup(ageInGroup);

        FeedbackContent formalInformal = new FeedbackContent();
        formalInformal.setContent("Formality feedback");
        feedback.setFormalInformal(formalInformal);

        feedbackRepository.save(feedback); // MongoDB에 저장
    }

    public void saveMonthlyFeedbackSample() {
        MonthlyFeedback monthlyFeedback = new MonthlyFeedback();
        monthlyFeedback.setFeedbackId("1");
        monthlyFeedback.setUserId("1");
        monthlyFeedback.setDate(new Date());

        FeedbackContent grammar = new FeedbackContent();
        grammar.setContent("Grammar monthly feedback");
        monthlyFeedback.setGrammar(grammar);

        FeedbackContent vocabulary = new FeedbackContent();
        vocabulary.setContent("Vocabulary monthly feedback");
        monthlyFeedback.setVocabulary(vocabulary);

        FeedbackContent ageInGroup = new FeedbackContent();
        ageInGroup.setContent("Age group monthly feedback");
        monthlyFeedback.setAgeInGroup(ageInGroup);

        FeedbackContent formalInformal = new FeedbackContent();
        formalInformal.setContent("Formality monthly feedback");
        monthlyFeedback.setFormalInformal(formalInformal);

        monthlyFeedbackRepository.save(monthlyFeedback); // MongoDB에 저장
    }

    public void saveChat() {
        ChatMessage chat = new ChatMessage();
        chat.setMsgId("12345");
        chat.setChatId(1);
        chat.setMsgType("user");
        chat.setMsgText("안녕하세요");
        chat.setMsgSound("S3 URL");


        chatRepository.save(chat); // MongoDB에 저장
    }
}
