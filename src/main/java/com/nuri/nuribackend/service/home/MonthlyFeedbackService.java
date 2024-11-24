package com.nuri.nuribackend.service.home;

import com.nuri.nuribackend.domain.ChatMessage;
import com.nuri.nuribackend.domain.Feedback.Feedback;
import com.nuri.nuribackend.domain.Feedback.MonthlyFeedback;
import com.nuri.nuribackend.dto.Feedback.MonthlyFeedbackFromGPT;
import com.nuri.nuribackend.repository.ChatMessageRepository;
import com.nuri.nuribackend.repository.ChatRepository;
import com.nuri.nuribackend.repository.FeedbackRepository;
import com.nuri.nuribackend.repository.MonthlyFeedbackRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@Slf4j
public class MonthlyFeedbackService {

    private final MonthlyFeedbackRepository monthlyFeedbackRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRepository chatRepository;
    private final FeedbackRepository feedbackRepository;
    private final OpenAiService openAiService;

    @Autowired
    public MonthlyFeedbackService(MonthlyFeedbackRepository monthlyFeedbackRepository,
                                  ChatMessageRepository chatMessageRepository,
                                  ChatRepository chatRepository,
                                  FeedbackRepository feedbackRepository,
                                  OpenAiService openAiService
                                  ) {
        this.monthlyFeedbackRepository = monthlyFeedbackRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.chatRepository = chatRepository;
        this.feedbackRepository = feedbackRepository;
        this.openAiService = openAiService;
    }

    public MonthlyFeedbackFromGPT getMonthlyFeedback(Long userId, int year, int month) {
        // 현재 년도, 월 구하기
        Calendar current = Calendar.getInstance();
        int currentYear = current.get(Calendar.YEAR);
        int currentMonth = current.get(Calendar.MONTH) + 1;     // 0부터 시작함 (0 = 1월, 11 = 12월)

        log.info("currentYear: " + currentYear);
        log.info("currentMonth: " + currentMonth);

        // 지난 달의 피드백 조회하는 경우
        if (year <= currentYear && month < currentMonth) {
            log.info("지난달 피드백 조회");
            return getPreviousMonthlyFeedback(userId, year, month);
        }

        // 현재 달의 피드백 조회하는 경우
        else if (year == currentYear && month == currentMonth) {
            log.info("현재달 피드백 조회");
            return getCurrentMonthlyFeedback(userId, year, month);
        }

        // 미래 달의 피드백 조회하는 경우
        else {
            MonthlyFeedbackFromGPT monthlyFeedback = new MonthlyFeedbackFromGPT();
            return monthlyFeedback;
        }
    }

    // 현재 월에대한 전체 피드백 조회 (mongodb 의 MonthlyFeedback)) -> chatGPT API 활용
    private MonthlyFeedbackFromGPT getCurrentMonthlyFeedback(Long userId, int year, int month) {
        MonthlyFeedbackFromGPT monthlyFeedbackFromGPT = new MonthlyFeedbackFromGPT();

        List<String> msgIds = new ArrayList<>();

        List<String> grammar = new ArrayList<>();
        List<String> vocabulary = new ArrayList<>();
        List<String> ageInGroup = new ArrayList<>();
        List<String> formalInformal = new ArrayList<>();

        // Chat 테이블에서 userId가 year.month에 대화한 채팅방ID(chatId) 목록 조회
        List<Integer> chatList = chatRepository.getChatIdList(userId, year, month);
        log.info("chatList: " + chatList);

        // ChatMessages 콜렉션에서 채팅방ID(chatId)로 MsgType이 user인 msgID 목록 조회
        for (Integer chatId : chatList) {
            for (ChatMessage m : chatMessageRepository.findByChatIdAndMsgType(chatId, "user")) {
                msgIds.add(m.getId());
            }
        }
        log.info("chatMessages: " + msgIds);

        // feedback 콜렉션에서 msgID로 grammar, vocabulary, ageInGroup, FormalInformal 조회
        for (String msgId : msgIds) {
            Feedback feedback = feedbackRepository.findByMsgId(msgId);

            grammar.add(feedback.getGrammar().getContent());
            vocabulary.add(feedback.getVocabulary().getContent());
            ageInGroup.add(feedback.getAgeInGroup().getContent());
            formalInformal.add(feedback.getFormalInformal().getContent());
        }

        log.info("grammar: " + grammar);
        log.info("vocabulary: " + vocabulary);
        log.info("ageInGroup: " + ageInGroup);
        log.info("formalInformal: " + formalInformal);

        // 추출한 피드백들을 chatGPT API에게 보냄
        monthlyFeedbackFromGPT.getGrammar().setContent(openAiService.getSummary("문법", grammar));
        monthlyFeedbackFromGPT.getVocabulary().setContent(openAiService.getSummary("어휘", vocabulary));
        monthlyFeedbackFromGPT.getAgeInGroup().setContent(openAiService.getSummary("10대, 20대, 30대 등 연령별 적절한 어휘 사용 여부", ageInGroup));
        monthlyFeedbackFromGPT.getFormalInformal().setContent(openAiService.getSummary("경어체", formalInformal));

        log.info("문법에 대한 피드백: " + monthlyFeedbackFromGPT.getGrammar().getContent());
        log.info("어휘에 대한 피드백: " + monthlyFeedbackFromGPT.getVocabulary().getContent());
        log.info("연령별 대화에 대한 피드백: " + monthlyFeedbackFromGPT.getAgeInGroup().getContent());
        log.info("경어체에 대한 피드백: " + monthlyFeedbackFromGPT.getFormalInformal().getContent());

        return monthlyFeedbackFromGPT;
    }

    // 지난 달에 대한 전체 피드백 조회(mongodb 의 MonthlyFeedback)
    private MonthlyFeedbackFromGPT getPreviousMonthlyFeedback(Long userId, int year, int month) {
        // userID, year, month 를 파라미터로 넘겨 MonthlyFeedback 콜렉션으로부터 월별 피드백 조회
        return monthlyFeedbackRepository.findByUserIdAndYearAndMonth(userId, year, month);
    }
}
