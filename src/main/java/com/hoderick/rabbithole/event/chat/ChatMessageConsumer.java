package com.hoderick.rabbithole.event.chat;

import com.hoderick.rabbithole.event.dto.ChatMessageEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageConsumer {

    @KafkaListener(topics = "chat-messages", groupId = "chat-consumer-group")
    public void listen(ChatMessageEvent event) {
        // TODO: Handle incoming messages.
        // Save to database.
        // Relay to any listeners currently listening.
        // Send Notification if no listeners (should be factored out into its own service and listening).
        System.out.println("Chat " + event.chatId() + ": " + event.message());
    }
}
