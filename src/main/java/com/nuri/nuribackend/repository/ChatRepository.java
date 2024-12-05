package com.nuri.nuribackend.repository;

import com.nuri.nuribackend.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    @Query("SELECT c.chatId FROM Chat c WHERE c.user.id = :userId AND EXTRACT(YEAR FROM c.date) = :year AND EXTRACT(MONTH FROM c.date) = :month")
    List<Integer> getChatIdList(Long userId, int year, int month);

    @Query("SELECT c FROM Chat c WHERE c.user.id = :userId")
    List<Chat> findAllByUserId(Integer userId);
}
