package com.hoderick.rabbithole.chat.mapper;

import com.hoderick.rabbithole.chat.dto.ChatDto;
import com.hoderick.rabbithole.chat.model.Chat;
import org.springframework.stereotype.Component;

@Component
public class ChatMapper {
    public ChatDto toDto(Chat chat) {
        return new ChatDto(chat.getId(), chat.getTitle());
    }
}
