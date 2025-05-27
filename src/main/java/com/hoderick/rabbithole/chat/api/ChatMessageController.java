package com.hoderick.rabbithole.chat.api;

import com.hoderick.rabbithole.chat.dto.ChatDto;
import com.hoderick.rabbithole.chat.dto.ChatTitle;
import com.hoderick.rabbithole.chat.dto.CreateChatRequest;
import com.hoderick.rabbithole.chat.dto.MessageDto;
import com.hoderick.rabbithole.chat.service.ChatFacade;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/chats")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageController {

    private final ChatFacade chatFacade;

    @PostMapping
    public ResponseEntity<UUID> createChat(@RequestBody CreateChatRequest request) {
        return ResponseEntity.ok(chatFacade.createChat(request.title(), request.userIds()));
    }

    @GetMapping
    public ResponseEntity<List<ChatDto>> getUserChats(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(chatFacade.getUserChats());
    }

    @GetMapping("/{chatId}/title")
    public ResponseEntity<ChatTitle> getChatTitle(@PathVariable UUID chatId) {
        return ResponseEntity.ok(chatFacade.getChatTitle(chatId));
    }

    @GetMapping("/{chatId}/messages")
    public Page<MessageDto> getRecentMessages(
            @PathVariable UUID chatId,
            @PageableDefault(size = 50, sort = "sentAt", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return chatFacade.getRecentMessages(chatId, pageable);
    }

}
