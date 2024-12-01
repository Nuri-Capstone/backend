package com.nuri.nuribackend.service;

import com.nuri.nuribackend.domain.Chat;
import com.nuri.nuribackend.domain.ChatMessage;
import com.nuri.nuribackend.domain.Feedback.Feedback;
import com.nuri.nuribackend.domain.Feedback.FeedbackContent;
import com.nuri.nuribackend.domain.Feedback.MonthlyFeedback;
import com.nuri.nuribackend.domain.User;
import com.nuri.nuribackend.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

// 데이터 저장 확인용 서비스임! (실제 서비스 X)

@Service
@Slf4j
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
    ChatMessage chatMessage;
    ArrayList<String> chatMessageList = new ArrayList<>();
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

        for (int i = 0 ; i < 4; i++) {

            chatMessage = new ChatMessage();

            chatMessage.setChatId(chat.getChatId());

            chatMessage.setMsgType("user");
            chatMessage.setMsgText("안녕 GPT야! 한국어 배우고 싶어!");
            chatMessage.setMsgSound("S3 URL");

            chatMessageRepository.save(chatMessage); // MongoDB에 저장

            chatMessageList.add(chatMessage.getId());
            log.info(chatMessage.getId());
        }
    }

    public void saveFeedbackSample() {

        // 문법 예시 피드백
        String[] grammarFeedbacks = new String[4];
        grammarFeedbacks[0] = "문장에서 은/는과 이/가를 혼용하지 않도록 주의하세요. 은/는은 주제를 강조하고, 이/가는 주어를 강조합니다.";
        grammarFeedbacks[1] = "조사를 빼먹지 마세요. 예를 들어 \"학교 가다\" 대신 \"학교에 가다\"가 올바릅니다.";
        grammarFeedbacks[2] = "-고 있다 형태는 현재 진행형을 나타내므로, 시간 표현과 일치해야 합니다.";
        grammarFeedbacks[3] = "부정문을 만들 때 안과 못의 차이를 구분하세요.";

        // 어휘 예시 피드백
        String[] vocabularyFeedbacks = new String[4];
        vocabularyFeedbacks[0] = "중복된 어휘를 줄이고 다양한 동의어를 사용해 보세요.";
        vocabularyFeedbacks[1] = "-스럽다와 -답다의 미묘한 차이를 이해해 보세요.";
        vocabularyFeedbacks[2] = "복합어를 적절히 활용하면 자연스러운 표현이 됩니다.";
        vocabularyFeedbacks[3] = "단어 선택이 문맥에 맞지 않아요. \"시작하다\"와 \"개시하다\"는 비슷하지만 상황에 따라 다릅니다.";

        // 경어체 예시 피드백
        String[] formalInformalFeedbacks = new String[4];
        formalInformalFeedbacks[0] = "높임말에서 간접 높임도 신경 쓰세요.";
        formalInformalFeedbacks[1] = "상대방의 이름 대신 직함을 사용하세요.";
        formalInformalFeedbacks[2] = "단순한 동사에도 높임을 추가하세요.";
        formalInformalFeedbacks[3] = "어른에게는 존댓말을 반드시 사용하세요.";

        for (int i = 0; i < 4; i++) {

            Feedback feedback = new Feedback();
            feedback.setMsgId(chatMessageList.get(i));

            FeedbackContent grammar = new FeedbackContent();
            grammar.setContent(grammarFeedbacks[i]);
            feedback.setGrammar(grammar);

            FeedbackContent vocabulary = new FeedbackContent();
            vocabulary.setContent(vocabularyFeedbacks[i]);
            feedback.setVocabulary(vocabulary);

            FeedbackContent formalInformal = new FeedbackContent();
            formalInformal.setContent(formalInformalFeedbacks[i]);
            feedback.setFormalInformal(formalInformal);

            feedbackRepository.save(feedback); // MongoDB에 저장
        }
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

        FeedbackContent formalInformal = new FeedbackContent();
        formalInformal.setContent("Formality monthly feedback");
        monthlyFeedback.setFormalInformal(formalInformal);

        monthlyFeedbackRepository.save(monthlyFeedback); // MongoDB에 저장
    }
}
