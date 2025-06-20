package com.hoderick.rabbithole.chat.service;

import com.hoderick.rabbithole.chat.dto.ChatDto;
import com.hoderick.rabbithole.chat.dto.ChatTitle;
import com.hoderick.rabbithole.chat.dto.MessageDto;
import com.hoderick.rabbithole.chat.dto.MessageReceivedDto;
import com.hoderick.rabbithole.event.dto.ChatMessageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatFacade {

    private final ChatService chatService;
    private final ChatMetadataService chatMetadataService;
    private final ChatMessageService chatMessageService;

    public UUID createChat(String title, Set<String> userIds) {
        return chatService.createChat(title, userIds);
    }

    public List<ChatDto> getUserChats() {
        return chatService.getUserChats();
    }

    public ChatTitle getChatTitle(UUID chatId) {
        return new ChatTitle(chatMetadataService.getChatTitle(chatId));
    }

    public Page<MessageDto> getRecentMessages(UUID chatId, Pageable pageable) {
        return chatMessageService.getRecentMessages(chatId, pageable);
    }

    public void processIncomingMessage(String chatId, String fromUserId, MessageReceivedDto dto) {
        // TODO: Any Validation
        chatMessageService.publishMessageEventToBroker(UUID.fromString(chatId), fromUserId, dto);
    }

    public void processDeliveryMessage(UUID chatId, ChatMessageEvent event) {
        MessageDto messageDto = chatMessageService.persistChatMessage(chatId, event);
        chatMessageService.sendMessageToClients(chatId, messageDto);
    }

}
