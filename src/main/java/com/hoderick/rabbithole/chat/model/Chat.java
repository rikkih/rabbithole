package com.hoderick.rabbithole.chat.model;

import com.hoderick.rabbithole.audit.model.Auditable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "chats")
public class Chat extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Getter
    private UUID id;

    @Setter
    private String title;

    @Setter
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatParticipant> chatParticipants = new ArrayList<>();

    @Setter
    @OneToMany(mappedBy = "chat")
    private List<Message> messages = new ArrayList<>();

}
