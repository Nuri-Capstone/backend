package com.nuri.nuribackend.service;

import com.nuri.nuribackend.domain.Chat;
import com.nuri.nuribackend.domain.ChatMessage;
import com.nuri.nuribackend.domain.Feedback.Feedback;
import com.nuri.nuribackend.domain.Feedback.FeedbackContent;
import com.nuri.nuribackend.domain.Feedback.MonthlyFeedback;
import com.nuri.nuribackend.domain.User;
import com.nuri.nuribackend.repository.*;
import org.springframework.stereotype.Service;

import java.util.Date;

// 데이터 저장 확인용 서비스임! (실제 서비스 X)

@Service
public class SampleService {

    private final FeedbackRepository feedbackRepository;
    private final MonthlyFeedbackRepository monthlyFeedbackRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public SampleService(FeedbackRepository feedbackRepository,
                         MonthlyFeedbackRepository monthlyFeedbackRepository,
                         ChatMessageRepository chatMessageRepository,
                         ChatRepository chatRepository,
                         UserRepository userRepository) {
        this.feedbackRepository = feedbackRepository;
        this.monthlyFeedbackRepository = monthlyFeedbackRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    User user = new User();
    Chat chat = new Chat();
    ChatMessage chatMessage = new ChatMessage();
    Feedback feedback = new Feedback();
    MonthlyFeedback monthlyFeedback = new MonthlyFeedback();

    public void saveUser() {

        user.setUserId("nuri");
        user.setEmail("nuri@gmail.com");
        user.setPassword("nuri");
        user.setName("nuri");
        user.setAge(24);

        userRepository.save(user);
    }
    public void saveChat() {

        chat.setDate(new Date());
        chat.setChatId(1);
        chat.setSubject("채팅방 제목");
        chat.setUserMsgCnt(10);
        chat.setSummary("채팅방 요약");
        chat.setUser(user);

        chatRepository.save(chat);
    }

    public void saveChatMessage() {

        chatMessage.setChatId(chat.getChatId());
        chatMessage.setMsgType("user");
        chatMessage.setMsgText("안녕 GPT야! 한국어 배우고 싶어!");
        chatMessage.setMsgSound("S3 URL");

        chatMessageRepository.save(chatMessage); // MongoDB에 저장
    }

    public void saveFeedbackSample() {

        feedback.setMsgId(chatMessage.getId());

        FeedbackContent grammar = new FeedbackContent();
        grammar.setContent("문법에 대한 피드백");
        feedback.setGrammar(grammar);

        FeedbackContent vocabulary = new FeedbackContent();
        vocabulary.setContent("어휘에 대한 피드백");
        feedback.setVocabulary(vocabulary);

        FeedbackContent ageInGroup = new FeedbackContent();
        ageInGroup.setContent("연령별 어휘에 대한 피드백");
        feedback.setAgeInGroup(ageInGroup);

        FeedbackContent formalInformal = new FeedbackContent();
        formalInformal.setContent("경어체에 대한 피드백");
        feedback.setFormalInformal(formalInformal);

        feedbackRepository.save(feedback); // MongoDB에 저장
    }

    public void saveMonthlyFeedbackSample() {

        monthlyFeedback.setUserId(user.getId());
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
}
