package com.example.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    
    @Id
    private String id;
    
    private String type;

    private String sender;
    private String receiver;
    private Object data;
    private String channelId;

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Message(String type, String sender, String receiver, Object data, String channelId) {
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.data = data;
        this.channelId = channelId;
    }

    public void newConnect() {
        this.type = "new";
    }

    public void closeConnect() {
        this.type = "close";
    }
    
}