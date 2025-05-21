package com.hoderick.rabbithole.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    private String from;
    private String text;
    private String timestamp;
    // TODO: This should probably be picked up from the DB. Check if this is worthwhile. We'll need to do a GET for each
    // websocket send. Perhaps we can just use the chat page to render the user image.
    private String avatarUrl;
}
