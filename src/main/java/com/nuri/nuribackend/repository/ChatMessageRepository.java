package com.nuri.nuribackend.repository;

import com.nuri.nuribackend.domain.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    @Query(value = "{ 'chatId' : ?0, 'msgType' : ?1 }")
    List<ChatMessage> findByChatIdAndMsgType(Integer chatId, String msgType);

    @Query(value = "{ 'chatId' : ?0 }")
    List<ChatMessage> findAllByChatId(Integer chatId);

}
