package com.hoderick.rabbithole.chat.repository;

import com.hoderick.rabbithole.chat.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    Page<Message> findByChatIdOrderBySentAtAsc(UUID chatId, Pageable pageable);
}
