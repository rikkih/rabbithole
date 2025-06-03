package com.hoderick.rabbithole.chat.api;

import com.hoderick.rabbithole.chat.dto.MessageReceivedDto;
import com.hoderick.rabbithole.chat.service.ChatFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.time.Instant;
import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChatWebSocketControllerTest {

    @Mock
    private ChatFacade chatFacade;

    @InjectMocks
    private ChatWebSocketController controller;

    @Test
    void handleIncomingMessage_shouldDelegateToChatFacade() {
        // Arrange
        String chatId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        Instant fixedTime = Instant.parse("2025-01-01T10:00:00Z");

        MessageReceivedDto messageDto = new MessageReceivedDto("Hello world!", fixedTime);
        Principal mockPrincipal = () -> userId;

        // Act
        controller.handleIncomingMessage(chatId, messageDto, mockPrincipal);

        // Assert
        verify(chatFacade).processIncomingMessage(chatId, userId, messageDto);
    }

}