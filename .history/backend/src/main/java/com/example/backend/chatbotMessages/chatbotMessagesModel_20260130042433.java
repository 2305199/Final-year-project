package com.example.backend.chatbotMessages;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "chatbot_messages")
public class chatbotMessagesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // DB column: TIMESTAMP, nullable
    @Column(name = "date", nullable = true)
    private Instant date;

    // DB column: TEXT (not VARCHAR)
    @Column(name = "message_text", columnDefinition = "TEXT", nullable = true)
    private String messageText;

    public Long getId() {
        return id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
