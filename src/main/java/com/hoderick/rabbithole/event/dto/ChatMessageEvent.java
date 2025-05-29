package com.hoderick.rabbithole.event.dto;

import java.time.Instant;
import java.util.UUID;

public record ChatMessageEvent(
        UUID chatId,
        String fromUserId,
        String message,
        Instant timeStamp
) {
}
