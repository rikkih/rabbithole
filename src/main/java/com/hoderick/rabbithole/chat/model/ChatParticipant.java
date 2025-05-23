package com.hoderick.rabbithole.chat.model;

import com.hoderick.rabbithole.audit.model.Auditable;
import com.hoderick.rabbithole.user.model.UserProfile;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@NoArgsConstructor
@Table(name = "chat_participants", indexes = {
        @Index(name = "idx_chat_participant_chat_id", columnList = "chat_id"),
        @Index(name = "idx_chat_participant_user_id", columnList = "user_id"),
})
public class ChatParticipant extends Auditable {

    @EmbeddedId
    private ChatParticipantId id;

    @ManyToOne
    @MapsId("chatId")
    private Chat chat;

    @ManyToOne
    @MapsId("userId")
    private UserProfile user;

    @Setter
    private Instant joinedAt;

    public ChatParticipant(Chat chat, UserProfile userProfile) {
        this.chat = chat;
        this.user = userProfile;
    }

}
