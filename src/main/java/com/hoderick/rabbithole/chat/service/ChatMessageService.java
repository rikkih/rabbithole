package com.hoderick.rabbithole.chat.service;

import com.hoderick.rabbithole.chat.dto.MessageDto;
import com.hoderick.rabbithole.chat.mapper.MessageMapper;
import com.hoderick.rabbithole.chat.repository.MessageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
class ChatMessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    Page<MessageDto> getRecentMessages(UUID chatId, Pageable pageable) {
        return messageRepository.findByChatIdOrderBySentAtAsc(chatId, pageable)
                .map(messageMapper::toDto);
    }
}
