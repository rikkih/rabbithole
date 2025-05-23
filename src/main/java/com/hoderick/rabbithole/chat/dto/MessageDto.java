package com.hoderick.rabbithole.chat.dto;

import java.time.Instant;
import java.util.UUID;

public record MessageDto(UUID chatId, String userId, String text, Instant sentAt) {
}
