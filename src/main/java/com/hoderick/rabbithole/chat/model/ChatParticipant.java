package com.hoderick.rabbithole.chat.model;

import com.hoderick.rabbithole.audit.model.Auditable;
import com.hoderick.rabbithole.user.model.UserProfile;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "chat_participants")
@NoArgsConstructor
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
