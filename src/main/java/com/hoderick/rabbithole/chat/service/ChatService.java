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
import com.hoderick.rabbithole.exception.NotFoundException;
import com.hoderick.rabbithole.user.model.UserProfile;
import com.hoderick.rabbithole.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;
    private final MessageMapper messageMapper;
    private final MessageRepository messageRepository;
    private final UserProfileService userProfileService;

    @Transactional
    public UUID createChat(String title, List<String> userIds) {
        Chat chat = new Chat();
        chat.setTitle(title);
        chat = chatRepository.save(chat);

        List<UserProfile> participants = userProfileService.getUserProfiles(userIds);

        for (UserProfile user : participants) {
            ChatParticipant participant = new ChatParticipant(chat, user);
            participant.setJoinedAt(Instant.now());
            chatParticipantRepository.save(participant);
        }

        return chat.getId();
    }

    List<ChatDto> getUserChats() {
        String principal = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Chat> chats = chatRepository.findByUserIdOrderByCreatedAtDesc(principal);
        return chats.stream()
                .map(chatMapper::toDto)
                .toList();
    }

    @Transactional
    // TODO: Can we read this in as a UUID on Spring controller?
    public MessageDto sendMessage(String chatId, String fromUserId, MessageReceivedDto dto) {
        Chat chat = chatRepository.findById(UUID.fromString(chatId))
                .orElseThrow(() -> new NotFoundException("Chat not found for: " + chatId));

        UserProfile userProfile = userProfileService.getUser(fromUserId);

        Message message = new Message(chat, userProfile, dto.text(), Instant.now());
        return messageMapper.toDto(messageRepository.save(message));
    }
}
