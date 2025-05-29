package com.hoderick.rabbithole.event.dto;

import java.time.Instant;

public record ChatMessageEvent(
        String fromUserId,
        String message,
        Instant timeStamp
) {
}
