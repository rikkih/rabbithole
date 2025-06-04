package com.hoderick.rabbithole.chat.dto;

import java.util.Set;

public record CreateChatRequest(String title, Set<String> userIds) {
}
