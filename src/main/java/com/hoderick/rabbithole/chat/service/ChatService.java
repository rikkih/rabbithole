package com.hoderick.rabbithole.chat.service;

import com.hoderick.rabbithole.chat.dto.MessageDto;
import com.hoderick.rabbithole.chat.mapper.MessageMapper;
import com.hoderick.rabbithole.chat.model.Chat;
import com.hoderick.rabbithole.chat.model.ChatParticipant;
import com.hoderick.rabbithole.chat.model.Message;
import com.hoderick.rabbithole.chat.repository.ChatParticipantRepository;
import com.hoderick.rabbithole.chat.repository.ChatRepository;
import com.hoderick.rabbithole.chat.repository.MessageRepository;
import com.hoderick.rabbithole.user.UserProfile;
import com.hoderick.rabbithole.user.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatRepository chatRepository;
    private final MessageMapper messageMapper;
    private final MessageRepository messageRepository;
    private final UserProfileService userProfileService;

    @Transactional
    public Chat createChat(String title, List<UserProfile> participants) {
        Chat chat = new Chat();
        chat.setTitle(title);
        chat = chatRepository.save(chat);

        for (UserProfile user : participants) {
            ChatParticipant participant = new ChatParticipant(chat, user);
            participant.setJoinedAt(Instant.now());
            chatParticipantRepository.save(participant);
        }

        return chat;
    }

    public List<Message> getMessages(Chat chat) {
        return messageRepository.findByChatIdOrderBySentAtAsc(chat.getId());
    }

    @Transactional
    public MessageDto sendMessage(MessageDto dto) {
        // TODO: NotFoundException
        Chat chat = chatRepository.findById(dto.chatId()).orElseThrow();
        // TODO: What if a user is anonymous or not found?
        UserProfile userProfile = userProfileService.getCurrentUser();

        Message message = new Message(chat, userProfile, dto.text(), Instant.now());
        return messageMapper.toDto(messageRepository.save(message));
    }
}
