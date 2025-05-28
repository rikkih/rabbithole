package com.hoderick.rabbithole.chat.service;

import com.hoderick.rabbithole.chat.dto.ChatDto;
import com.hoderick.rabbithole.chat.dto.MessageDto;
import com.hoderick.rabbithole.chat.dto.MessageReceivedDto;
import com.hoderick.rabbithole.chat.mapper.ChatMapper;
import com.hoderick.rabbithole.chat.mapper.MessageMapper;
import com.hoderick.rabbithole.chat.model.Chat;
import com.hoderick.rabbithole.chat.model.ChatParticipant;
import com.hoderick.rabbithole.chat.model.Message;
import com.hoderick.rabbithole.chat.repository.ChatParticipantRepository;
import com.hoderick.rabbithole.chat.repository.ChatRepository;
import com.hoderick.rabbithole.chat.repository.MessageRepository;
import com.hoderick.rabbithole.user.model.UserProfile;
import com.hoderick.rabbithole.user.service.UserProfileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatParticipantRepository chatParticipantRepository;
    @Mock
    private ChatRepository chatRepository;
    @Spy
    private ChatMapper chatMapper;
    @Mock
    private MessageMapper messageMapper;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private UserProfileService userProfileService;

    @InjectMocks
    private ChatService chatService;

    @Test
    void createChat_shouldCreateChatAndSaveParticipants() {
        String title = "Test Chat";
        List<String> userIds = List.of("user1", "user2");

        Chat savedChat = mock(Chat.class);
        UUID expectedChatId = UUID.randomUUID();
        when(savedChat.getId()).thenReturn(expectedChatId);
        when(chatRepository.save(any(Chat.class))).thenReturn(savedChat);

        UserProfile user1 = new UserProfile("user1");
        UserProfile user2 = new UserProfile("user2");
        when(userProfileService.getUserProfiles(userIds)).thenReturn(List.of(user1, user2));

        UUID chatId = chatService.createChat(title, userIds);

        assertEquals(expectedChatId, chatId);
        verify(chatRepository).save(any(Chat.class));
        verify(chatParticipantRepository, times(2)).save(any(ChatParticipant.class));
    }


    @Test
    void getUserChats_shouldReturnMappedChatDtos() {
        String currentUserId = "user123";
        List<Chat> chats = List.of(new Chat(), new Chat());

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(currentUserId, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(chatRepository.findByUserIdOrderByCreatedAtDesc(currentUserId)).thenReturn(chats);

        List<ChatDto> result = chatService.getUserChats();

        assertEquals(2, result.size());
        verify(chatRepository).findByUserIdOrderByCreatedAtDesc(currentUserId);

        // Clean up context
        SecurityContextHolder.clearContext();
    }


    @Test
    void sendMessage_shouldSaveMessageAndReturnDto() {
        // Given
        UUID chatId = UUID.randomUUID();
        String userId = "user-123";
        String messageText = "Hello!";
        Instant sentAt = Instant.parse("2024-01-01T12:00:00Z");
        MessageReceivedDto receivedDto = new MessageReceivedDto(messageText, sentAt);

        Chat mockChat = new Chat(); // Or mock(Chat.class) if needed
        UserProfile mockUser = new UserProfile(userId);
        Message savedMessage = new Message(mockChat, mockUser, messageText, Instant.now());

        MessageDto expectedDto = new MessageDto(UUID.randomUUID(), userId, messageText, Instant.now());

        when(chatRepository.findById(chatId)).thenReturn(Optional.of(mockChat));
        when(userProfileService.getUser(userId)).thenReturn(mockUser);
        when(messageRepository.save(any(Message.class))).thenReturn(savedMessage);
        when(messageMapper.toDto(savedMessage)).thenReturn(expectedDto);

        // When
        MessageDto actualDto = chatService.sendMessage(chatId.toString(), userId, receivedDto);

        // Then
        assertEquals(expectedDto, actualDto);
        verify(chatRepository).findById(chatId);
        verify(userProfileService).getUser(userId);
        verify(messageRepository).save(any(Message.class));
        verify(messageMapper).toDto(savedMessage);
    }
}