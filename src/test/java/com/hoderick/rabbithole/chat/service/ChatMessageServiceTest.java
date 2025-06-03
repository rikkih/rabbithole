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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatMessageServiceTest {

    @Mock
    private ChatRepository chatRepository;
    @Mock
    private ChatMessageProducer chatMessageProducer;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private MessageMapper messageMapper;
    @Mock
    private UserProfileService userProfileService;
    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private ChatMessageService chatMessageService;

    @Test
    void whenGetRecentMessages_shouldReturnMappedMessages() {
        // Arrange
        UUID chatId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 20);
        Message message = mock(Message.class);
        MessageDto dto = new MessageDto(UUID.randomUUID(), "userId1", "Hey", Instant.parse("2025-01-01T10:00:00Z"));

        Page<Message> page = new PageImpl<>(List.of(message), pageable, 1);
        when(messageRepository.findByChatIdOrderBySentAtAsc(chatId, pageable)).thenReturn(page);
        when(messageMapper.toDto(message)).thenReturn(dto);

        // Act
        Page<MessageDto> result = chatMessageService.getRecentMessages(chatId, pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals(dto, result.getContent().get(0));
        verify(messageRepository).findByChatIdOrderBySentAtAsc(chatId, pageable);
        verify(messageMapper).toDto(message);
    }

    @Test
    void whenPublishMessageEventToBroker_shouldSendEvent() {
        // Arrange
        UUID chatId = UUID.randomUUID();
        String userId = "user123";
        MessageReceivedDto dto = new MessageReceivedDto("Hello", Instant.parse("2025-01-01T10:00:00Z"));

        // Act
        chatMessageService.publishMessageEventToBroker(chatId, userId, dto);

        // Assert
        ChatMessageEvent expectedEvent = new ChatMessageEvent(userId, dto.text(), dto.sentAt());
        verify(chatMessageProducer).send(eq(chatId), eq(expectedEvent));
    }

    @Test
    void persistChatMessage_shouldPersistMessageAndReturnDto() {
        // Arrange
        UUID chatId = UUID.randomUUID();
        String userId = "user456";
        Chat chat = mock(Chat.class);
        UserProfile userProfile = mock(UserProfile.class);
        Message message = mock(Message.class);
        Instant sentAt = Instant.now();
        String text = "Hello world";

        ChatMessageEvent event = new ChatMessageEvent(userId, text, sentAt);
        MessageDto dto = new MessageDto(UUID.randomUUID(), "userId1", text, sentAt);

        when(chatRepository.findById(chatId)).thenReturn(Optional.of(chat));
        when(userProfileService.getUser(userId)).thenReturn(userProfile);
        when(messageRepository.save(any(Message.class))).thenReturn(message);
        when(messageMapper.toDto(message)).thenReturn(dto);

        // Act
        MessageDto result = chatMessageService.persistChatMessage(chatId, event);

        // Assert
        assertEquals(dto, result);
        verify(chatRepository).findById(chatId);
        verify(userProfileService).getUser(userId);
        verify(messageRepository).save(any(Message.class));
        verify(messageMapper).toDto(message);
    }

    @Test
    void persistChatMessage_shouldThrow_whenChatNotFound() {
        // Arrange
        UUID chatId = UUID.randomUUID();
        ChatMessageEvent event = new ChatMessageEvent("userX", "msg", Instant.now());

        when(chatRepository.findById(chatId)).thenReturn(Optional.empty());

        // Act + Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                chatMessageService.persistChatMessage(chatId, event)
        );
        assertEquals(exception.getMessage(), "Chat not found for: " + chatId);
    }

    @Test
    void sendMessageToClients_shouldSendMessageToCorrectTopic() {
        // Arrange
        UUID chatId = UUID.randomUUID();
        MessageDto dto = new MessageDto(UUID.randomUUID(), "userId", "Hey", Instant.parse("2025-01-01T10:00:00Z"));

        // Act
        chatMessageService.sendMessageToClients(chatId, dto);

        // Assert
        verify(messagingTemplate).convertAndSend("/topic/chat." + chatId, dto);
    }
}