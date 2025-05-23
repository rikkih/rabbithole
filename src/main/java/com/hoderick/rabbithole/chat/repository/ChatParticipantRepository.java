package com.hoderick.rabbithole.chat.repository;

import com.hoderick.rabbithole.chat.model.ChatParticipant;
import com.hoderick.rabbithole.chat.model.ChatParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, ChatParticipantId> {
}
