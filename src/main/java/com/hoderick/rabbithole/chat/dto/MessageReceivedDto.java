package com.hoderick.rabbithole.chat.dto;

import java.time.Instant;

public record MessageReceivedDto(String text, Instant sentAt) {
}
