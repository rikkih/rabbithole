package com.hoderick.rabbithole.chat.service;

import com.hoderick.rabbithole.chat.model.Chat;
import com.hoderick.rabbithole.chat.repository.ChatRepository;
import com.hoderick.rabbithole.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
class ChatMetadataService {

    private final ChatRepository chatRepository;

    String getChatTitle(UUID chatId) {
        return chatRepository.findById(chatId)
                .map(Chat::getTitle)
                .orElseThrow(() -> new NotFoundException("Chat not found for: " + chatId));
    }
}
