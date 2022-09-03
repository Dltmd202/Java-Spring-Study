package com.example.mongows;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MessageService {
    
    private final MessageRepository messageRepository;
    
    public Message create(Message message){
        messageRepository.save(message);
        return message;
    }
    
}