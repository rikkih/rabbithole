package com.hoderick.rabbithole.chat.repository;

import com.hoderick.rabbithole.chat.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findByChatIdOrderBySentAtAsc(UUID chatId);
}
