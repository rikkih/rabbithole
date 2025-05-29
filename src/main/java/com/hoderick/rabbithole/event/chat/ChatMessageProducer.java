package com.hoderick.rabbithole.event.chat;

import com.hoderick.rabbithole.event.dto.ChatMessageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatMessageProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(ChatMessageEvent event) {
        // TODO: Make topic configurable
        // TODO: Could the event be stripped of chatId since we used that as the Key!
        kafkaTemplate.send("chat-messages", event.chatId().toString(), event);
    }
}
