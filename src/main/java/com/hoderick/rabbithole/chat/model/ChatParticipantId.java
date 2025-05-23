package com.hoderick.rabbithole.chat.model;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class ChatParticipantId {
    private UUID chatId;
    private String userId;
}
