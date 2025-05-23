package com.hoderick.rabbithole.chat.service;

import com.hoderick.rabbithole.chat.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatFacade {

    private final ChatService chatService;
    private final ChatMessageService chatMessageService;

    public Page<MessageDto> getRecentMessages(UUID chatId, Pageable pageable) {
        return chatMessageService.getRecentMessages(chatId, pageable);
    }
}
