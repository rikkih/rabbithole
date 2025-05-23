package com.hoderick.rabbithole.chat.model;

import com.hoderick.rabbithole.audit.model.Auditable;
import com.hoderick.rabbithole.user.model.UserProfile;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "messages", indexes = {
        @Index(name = "idx_message_chat_id", columnList = "chat_id"),
        @Index(name = "idx_message_chat_id_sent_at", columnList = "chat_id, sent_at"),
})
public class Message extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Chat chat;

    @ManyToOne
    private UserProfile sender;

    private String text;

    private Instant sentAt;

    @Setter
    private boolean deleted;

    public Message(Chat chat, UserProfile sender, String text, Instant sentAt) {
        this.chat = chat;
        this.sender = sender;
        this.text = text;
        this.sentAt = sentAt;
    }

}
