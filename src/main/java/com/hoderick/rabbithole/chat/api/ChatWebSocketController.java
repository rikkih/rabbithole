package com.hoderick.rabbithole.chat.api;

import com.hoderick.rabbithole.chat.dto.MessageDto;
import com.hoderick.rabbithole.chat.dto.MessageReceivedDto;
import com.hoderick.rabbithole.chat.service.ChatService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatWebSocketController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.{chatId}")
    public void send(@DestinationVariable String chatId, MessageReceivedDto dto, Principal principal) {
        String authenticatedUserId = principal.getName();
        MessageDto saved = chatService.sendMessage(chatId, authenticatedUserId, dto);
        messagingTemplate.convertAndSend("/topic/chat." + chatId, saved);
    }
}
