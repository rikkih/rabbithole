package com.hoderick.rabbithole.chat.service;

import com.hoderick.rabbithole.chat.dto.ChatDto;
import com.hoderick.rabbithole.chat.dto.ChatTitle;
import com.hoderick.rabbithole.chat.dto.MessageDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatFacadeTest {

    @Mock
    private ChatService chatService;

    @Mock
    private ChatMetadataService chatMetadataService;

    @Mock
    private ChatMessageService chatMessageService;

    @InjectMocks
    private ChatFacade uut;

    @Test
    void createChat() {
        Set<String> userIds = Set.of("userID");
        String chatTitle = "title";
        UUID expectedChatId = UUID.randomUUID();

        when(chatService.createChat(anyString(), anySet())).thenReturn(expectedChatId);

        UUID actualChatId = uut.createChat(chatTitle, userIds);

        verify(chatService).createChat(chatTitle, userIds);
        assertEquals(expectedChatId, actualChatId);
    }

    @Test
    void getUserChats_shouldReturnUserChatsFromService() {
        List<ChatDto> expectedChats = List.of(
                new ChatDto(UUID.randomUUID(), "Chat 1"),
                new ChatDto(UUID.randomUUID(), "Chat 2")
        );

        when(chatService.getUserChats()).thenReturn(expectedChats);

        List<ChatDto> actualChats = uut.getUserChats();

        verify(chatService).getUserChats();
        assertEquals(expectedChats, actualChats);
    }


    @Test
    void getChatTitle_shouldWrapChatTitleFromMetadataService() {
        UUID chatId = UUID.randomUUID();
        String expectedTitle = "Test Title";

        when(chatMetadataService.getChatTitle(chatId)).thenReturn(expectedTitle);

        ChatTitle actualChatTitle = uut.getChatTitle(chatId);

        verify(chatMetadataService).getChatTitle(chatId);
        assertEquals(expectedTitle, actualChatTitle.title());
    }


    @Test
    void getRecentMessages_shouldReturnMessagesFromService() {
        UUID chatId = UUID.randomUUID();
        Pageable pageable = Pageable.unpaged();
        Page<MessageDto> expectedPage = mock(Page.class);

        when(chatMessageService.getRecentMessages(chatId, pageable)).thenReturn(expectedPage);

        Page<MessageDto> actualPage = uut.getRecentMessages(chatId, pageable);

        verify(chatMessageService).getRecentMessages(chatId, pageable);
        assertEquals(expectedPage, actualPage);
    }
}