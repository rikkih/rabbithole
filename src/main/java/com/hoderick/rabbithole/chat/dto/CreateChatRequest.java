package com.hoderick.rabbithole.chat.dto;

import java.util.List;

public record CreateChatRequest(String title, List<String> userIds) {
}
