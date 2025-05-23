package com.hoderick.rabbithole.chat.repository;

import com.hoderick.rabbithole.chat.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
}
