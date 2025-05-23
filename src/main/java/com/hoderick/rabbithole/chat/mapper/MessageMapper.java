package com.hoderick.rabbithole.chat.mapper;

import com.hoderick.rabbithole.chat.dto.MessageDto;
import com.hoderick.rabbithole.chat.model.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {
    public MessageDto toDto(Message message) {
        return new MessageDto(
                message.getChat().getId(),
                message.getSender().getId(),
                message.getText(),
                message.getSentAt()
        );
    }
}
