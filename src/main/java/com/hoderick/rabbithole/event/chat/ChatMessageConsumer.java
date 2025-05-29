package com.hoderick.rabbithole.event.chat;

import com.hoderick.rabbithole.chat.service.ChatFacade;
import com.hoderick.rabbithole.event.dto.ChatMessageEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChatMessageConsumer {

    private final ChatFacade chatFacade;

    @KafkaListener(topics = "chat-messages", groupId = "chat-consumer-group")
    public void listen(ConsumerRecord<UUID, ChatMessageEvent> record) {
        // TODO: Check WebSocket Presence Service to see if user is online.
        //  Handle relaying to correct server for instant messaging.
        // TODO: Send Notification if no listeners (should be factored out into its own service and listening).

        chatFacade.processDeliveryMessage(record.key(), record.value());
    }
}
