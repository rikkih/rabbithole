package com.hoderick.rabbithole.chat.api;

import com.hoderick.rabbithole.chat.dto.MessageReceivedDto;
import com.hoderick.rabbithole.chat.service.ChatFacade;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatWebSocketController {

    private final ChatFacade chatFacade;

    @MessageMapping("/chat.{chatId}")
    public void handleIncomingMessage(@DestinationVariable String chatId, MessageReceivedDto dto, Principal principal) {
        String authenticatedUserId = principal.getName();
        chatFacade.processIncomingMessage(chatId, authenticatedUserId, dto);
    }
}
