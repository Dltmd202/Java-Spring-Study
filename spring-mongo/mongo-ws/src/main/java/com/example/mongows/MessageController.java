package com.example.mongows;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {
    
    private final MessageService messageService;
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    
    @MessageMapping("/hello")
    public void message(Message message){
        messageService.create(message);
        simpMessageSendingOperations.convertAndSend("/sub/channel/" + message.getChannelId(), message);
    }
}