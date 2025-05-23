package com.hoderick.rabbithole.chat.api;

import com.hoderick.rabbithole.chat.dto.MessageDto;
import com.hoderick.rabbithole.chat.service.ChatService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatWebSocketController {

    private final ChatService chatService;

    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    public MessageDto send(MessageDto dto, Principal principal) {
       return chatService.sendMessage(dto);
    }
}
