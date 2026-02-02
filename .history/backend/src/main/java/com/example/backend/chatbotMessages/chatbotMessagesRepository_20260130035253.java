package com.example.backend.chatbotMessages;

import org.springframework.data.jpa.repository.JpaRepository;

public interface chatbotMessagesRepository extends JpaRepository<chatbotMessagesModel, Long> {
}
