package com.hoderick.rabbithole.chat.api;

import com.hoderick.rabbithole.chat.dto.ChatDto;
import com.hoderick.rabbithole.chat.dto.ChatTitle;
import com.hoderick.rabbithole.chat.dto.CreateChatRequest;
import com.hoderick.rabbithole.chat.dto.MessageDto;
import com.hoderick.rabbithole.chat.service.ChatFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatMessageControllerTest {

    @Mock
    private ChatFacade chatFacade;
    @InjectMocks
    private ChatMessageController uut;

    @Test
    public void givenCreatingChat_shouldReturnChatId() {
        // Arrange
        CreateChatRequest request = new CreateChatRequest("My Chat", Set.of("user1Id", "user2Id"));
        UUID expectedId = UUID.randomUUID();

        when(chatFacade.createChat(request.title(), request.userIds())).thenReturn(expectedId);

        // Act
        ResponseEntity<UUID> response = uut.createChat(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedId, response.getBody());
    }

    @Test
    void givenGetUserChats_returnsChatDtos() {
        // Arrange
        List<ChatDto> chats = List.of(
                new ChatDto(UUID.randomUUID(), "Chat 1"),
                new ChatDto(UUID.randomUUID(), "Chat 2")
        );

        when(chatFacade.getUserChats()).thenReturn(chats);

        // Act
        ResponseEntity<List<ChatDto>> response = uut.getUserChats();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(chats, response.getBody());
    }

    @Test
    public void givenGetChatTitle_shouldReturnChatTitle() {
        // Arrange
        UUID chatId = UUID.randomUUID();
        ChatTitle expectedTitle = new ChatTitle("My Chat");

        when(chatFacade.getChatTitle(chatId)).thenReturn(expectedTitle);

        // Act
        ResponseEntity<ChatTitle> response = uut.getChatTitle(chatId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedTitle, response.getBody());
    }

    @Test
    public void givenGetRecentMessages_shouldReturnRecentMessagesPage() {
        // Arrange
        UUID chatId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 50, Sort.by(Sort.Direction.ASC, "sentAt"));
        Instant fixedTime = Instant.parse("2025-01-01T10:00:00Z");

        List<MessageDto> messages = List.of(
                new MessageDto(UUID.randomUUID(), "user1Id", "Hello", fixedTime),
                new MessageDto(UUID.randomUUID(), "user1Id", "How are you?", fixedTime)
        );
        Page<MessageDto> page = new PageImpl<>(messages, pageable, messages.size());

        when(chatFacade.getRecentMessages(chatId, pageable)).thenReturn(page);

        // Act
        Page<MessageDto> result = uut.getRecentMessages(chatId, pageable);

        // Assert
        assertEquals(2, result.getTotalElements());
        assertEquals(messages, result.getContent());
    }
}