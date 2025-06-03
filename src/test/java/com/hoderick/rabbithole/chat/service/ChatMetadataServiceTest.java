package com.hoderick.rabbithole.chat.service;

import com.hoderick.rabbithole.chat.model.Chat;
import com.hoderick.rabbithole.chat.repository.ChatRepository;
import com.hoderick.rabbithole.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ChatMetadataServiceTest {

    @Mock
    private ChatRepository chatRepository;
    @InjectMocks
    private ChatMetadataService uut;

    @Test
    public void whenGetChatTitle_shouldReturnTitle() {
        // Arrange
        UUID chatId = UUID.randomUUID();
        Chat chat = new Chat();
        chat.setTitle("Bossmen Tings");

        when(chatRepository.findById(chatId)).thenReturn(Optional.of(chat));

        // Act
        String result = uut.getChatTitle(chatId);

        // Assert
        assertEquals("Bossmen Tings", result);
    }

    @Test
    public void whenGetChatTitleForNonExistentChat_shouldThrowNotFoundException() {
        // Arrange
        UUID chatId = UUID.randomUUID();
        when(chatRepository.findById(chatId)).thenReturn(Optional.empty());

        // Act + Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> uut.getChatTitle(chatId)
        );

        assertEquals("Chat not found for: " + chatId, exception.getMessage());
    }

}