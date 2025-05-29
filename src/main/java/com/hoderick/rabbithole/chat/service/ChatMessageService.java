package com.hoderick.rabbithole.chat.service;

import com.hoderick.rabbithole.chat.dto.MessageDto;
import com.hoderick.rabbithole.chat.dto.MessageReceivedDto;
import com.hoderick.rabbithole.chat.mapper.MessageMapper;
import com.hoderick.rabbithole.chat.model.Chat;
import com.hoderick.rabbithole.chat.model.Message;
import com.hoderick.rabbithole.chat.repository.ChatRepository;
import com.hoderick.rabbithole.chat.repository.MessageRepository;
import com.hoderick.rabbithole.event.chat.ChatMessageProducer;
import com.hoderick.rabbithole.event.dto.ChatMessageEvent;
import com.hoderick.rabbithole.exception.NotFoundException;
import com.hoderick.rabbithole.user.model.UserProfile;
import com.hoderick.rabbithole.user.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
class ChatMessageService {

    private final ChatRepository chatRepository;
    private final ChatMessageProducer chatMessageProducer;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final UserProfileService userProfileService;
    private final SimpMessagingTemplate messagingTemplate;

    Page<MessageDto> getRecentMessages(UUID chatId, Pageable pageable) {
        return messageRepository.findByChatIdOrderBySentAtAsc(chatId, pageable)
                .map(messageMapper::toDto);
    }

    void publishMessageEventToBroker(UUID chatId, String userId, MessageReceivedDto dto) {
        ChatMessageEvent event = new ChatMessageEvent(userId, dto.text(), dto.sentAt());
        chatMessageProducer.send(chatId, event);
    }

    MessageDto persistChatMessage(UUID chatID, ChatMessageEvent event) {
        Chat chat = chatRepository.findById(chatID)
                .orElseThrow(() -> new NotFoundException("Chat not found for: " + chatID));

        UserProfile userProfile = userProfileService.getUser(event.fromUserId());

        Message message = new Message(chat, userProfile, event.message(), Instant.now());
        return messageMapper.toDto(messageRepository.save(message));
    }

    void sendMessageToClients(UUID chatId, MessageDto dto) {
        messagingTemplate.convertAndSend("/topic/chat." + chatId, dto);
    }
}
