package com.hoderick.rabbithole.chat.service;

import com.hoderick.rabbithole.chat.dto.ChatDto;
import com.hoderick.rabbithole.chat.mapper.ChatMapper;
import com.hoderick.rabbithole.chat.mapper.MessageMapper;
import com.hoderick.rabbithole.chat.model.Chat;
import com.hoderick.rabbithole.chat.model.ChatParticipant;
import com.hoderick.rabbithole.chat.repository.ChatParticipantRepository;
import com.hoderick.rabbithole.chat.repository.ChatRepository;
import com.hoderick.rabbithole.chat.repository.MessageRepository;
import com.hoderick.rabbithole.user.model.UserProfile;
import com.hoderick.rabbithole.user.service.UserProfileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
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
        // Arrange
        String title = "Test Chat";
        Set<String> userIds = new HashSet<>(List.of("user1Id"));

        String requestingUserId = "requestingUserId";
        TestingAuthenticationToken auth = new TestingAuthenticationToken(requestingUserId, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Chat savedChat = mock(Chat.class);
        UUID expectedChatId = UUID.randomUUID();
        when(savedChat.getId()).thenReturn(expectedChatId);
        when(chatRepository.save(any(Chat.class))).thenReturn(savedChat);

        UserProfile user1 = new UserProfile("user1Id");
        UserProfile requestingUser = new UserProfile("requestingUserId");
        when(userProfileService.getUserProfiles(anyList())).thenReturn(List.of(user1, requestingUser));

        // Act
        UUID chatId = chatService.createChat(title, userIds);

        // Assert
        ArgumentCaptor<List<String>> userListCaptor = ArgumentCaptor.forClass(List.class);

        assertEquals(expectedChatId, chatId);
        verify(chatRepository).save(any(Chat.class));
        verify(chatParticipantRepository, times(2)).save(any(ChatParticipant.class));
        verify(userProfileService).getUserProfiles(userListCaptor.capture());

        List<String> allUserIdsPassed = userListCaptor.getValue();
        assertThat(allUserIdsPassed).containsExactlyInAnyOrder("user1Id", "requestingUserId");

        // Cleanup
        SecurityContextHolder.clearContext();
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
}