package com.hoderick.rabbithole.chat.dto;

import java.time.Instant;
import java.util.UUID;

public record MessageDto(UUID id, String fromId, String text, Instant sentAt) {
}
