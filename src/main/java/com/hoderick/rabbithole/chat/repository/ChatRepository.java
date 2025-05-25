package com.hoderick.rabbithole.chat.repository;

import com.hoderick.rabbithole.chat.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
    @Query("SELECT c FROM Chat c JOIN c.chatParticipants cp WHERE cp.user.id = :userId ORDER BY c.createdAt DESC")
    List<Chat> findByUserIdOrderByCreatedAtDesc(@Param("userId") String userId);
}
