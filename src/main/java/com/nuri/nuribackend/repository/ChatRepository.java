package com.nuri.nuribackend.repository;

import com.nuri.nuribackend.domain.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends MongoRepository<ChatMessage, String> {
}
