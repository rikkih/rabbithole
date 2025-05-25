package com.hoderick.rabbithole.chat.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ChatParticipantId implements Serializable {
    @Column(name = "chat_id", nullable = false)
    private UUID chatId;

    @Column(name = "user_id", nullable = false)
    private String userId;
}
