package com.nuri.nuribackend.batch.tasklet;

import com.nuri.nuribackend.domain.ChatMessage;
import com.nuri.nuribackend.domain.Feedback.Feedback;
import com.nuri.nuribackend.domain.Feedback.FeedbackContent;
import com.nuri.nuribackend.domain.Feedback.MonthlyFeedback;
import com.nuri.nuribackend.repository.*;
import com.nuri.nuribackend.service.home.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Component
@Slf4j
public class MonthlyFeedbackTasklet implements Tasklet {

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final FeedbackRepository feedbackRepository;
    private final MonthlyFeedbackRepository monthlyFeedbackRepository;
    private final OpenAiService openAiService;

    public MonthlyFeedbackTasklet(UserRepository userRepository, ChatRepository chatRepository, ChatMessageRepository chatMessageRepository, FeedbackRepository feedbackRepository, MonthlyFeedbackRepository monthlyFeedbackRepository, OpenAiService openAiService) {
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.feedbackRepository = feedbackRepository;
        this.monthlyFeedbackRepository = monthlyFeedbackRepository;
        this.openAiService = openAiService;
    }

    @Override
    public RepeatStatus execute (StepContribution contribution, ChunkContext chunkContext) throws Exception {

        // 현재 월에 해당하는 데이터들을 배치처리 해주어야 하기 때문에 년도와 월을 구함
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;

        // 모든 사용자에 대해서 배치처리를 해주어야 하기 때문에 모든 userId 조회
        List<Long> userIds = userRepository.findAllUserIds();

        for (Long userId : userIds) {
            // 사용자가 현재 월에 대화한 채팅방 조회
            List<Integer> chatIds = chatRepository.getChatIdList(userId, year, month);

            /* 만약, 사용자가 현재 월에 학습을 하지 않은 경우 (생성된 채팅방이 없는 경우) -> 건너뜀
            만약, 사용자가 현재 월에 학습한 경우 -> 받은 피드백을 조회하고 구분해서 저장함 */
            if (chatIds.isEmpty()) {

                // 월별 피드백에서 조회한 값이 null인 경우 새로운 객체 생성해서 DB에 저장
                MonthlyFeedback feedback = monthlyFeedbackRepository.findByUserIdAndYearAndMonth(userId, year, month);

                if (feedback == null) {
                    MonthlyFeedback emptyFeedback = new MonthlyFeedback();

                    FeedbackContent content = new FeedbackContent();
                    content.setContent("피드백이 존재하지 않습니다.");

                    emptyFeedback.setGrammar(content);
                    emptyFeedback.setVocabulary(content);
                    emptyFeedback.setFormalInformal(content);

                    monthlyFeedbackRepository.save(emptyFeedback);
                }
            }

            else {
                List<String> grammar = new ArrayList<>();
                List<String> vocabulary = new ArrayList<>();
                List<String> formalInformal = new ArrayList<>();

                /* 학습한 채팅방(chatId)마다 아래 과정을 반복
                1. msgType이 user인 메시지들을 조회함
                2. 각 msgId로 feedbackRepository에서 피드백들을 조회함
                3. 위에서 생성한 피드백 리스트에 저장함 */

                for (Integer chatId : chatIds) {
                    // 채팅방에서 사용자가 보낸 메시지들 조회
                    List<ChatMessage> chatMessages = chatMessageRepository.findByChatIdAndMsgType(chatId, "user");

                    // 각 메시지 마다 달린 피드백들을 리스트(grammar, vocabulary,formalInformal)에 저장함
                    for (ChatMessage message : chatMessages) {
                        String msgId = message.getId();

                        Feedback feedback = feedbackRepository.findByMsgId(msgId);
                        if (feedback != null) {
                            grammar.add(feedback.getGrammar().getContent());
                            vocabulary.add(feedback.getVocabulary().getContent());
                            formalInformal.add(feedback.getFormalInformal().getContent());
                        }
                    }
                }

                // 각 사용자마다 조회한 feedback들 리스트를 gpt에게 보내고 요약 응답을 받음
                String grammarSummary = openAiService.getSummary("문법", grammar);
                String vocabularySummary = openAiService.getSummary("어휘", vocabulary);
                String formalInformalSummary = openAiService.getSummary("경어체", formalInformal);

                /* 기존 monthly_feedback 테이블에 값이 있는 경우 -> 갱신
                기존 monthly_feedback 테이블에 값이 없는 경우 -> 객체 생성 후 저장 */
                MonthlyFeedback monthlyFeedback = monthlyFeedbackRepository.findByUserIdAndYearAndMonth(userId, year, month);

                if (monthlyFeedback == null) {
                    monthlyFeedback = new MonthlyFeedback();
                }

                monthlyFeedback.getGrammar().setContent(grammarSummary);
                monthlyFeedback.getVocabulary().setContent(vocabularySummary);
                monthlyFeedback.getFormalInformal().setContent(formalInformalSummary);

                // monthly_feedback 테이블에 데이터 갱신
                monthlyFeedbackRepository.save(monthlyFeedback);

                log.info("월별 피드백 갱신");
            }
        }
        return RepeatStatus.FINISHED;
    }
}
