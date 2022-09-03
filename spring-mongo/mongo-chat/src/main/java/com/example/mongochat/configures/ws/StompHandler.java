package com.example.mongochat.configures.ws;

import com.sidepr.mono.sns.user.security.Jwt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {
    
    private final Jwt jwt;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        
        if(StompCommand.CONNECT.equals(accessor.getCommand())){
            // TODO error 변환
            Objects.requireNonNull(jwt.verify(
                    Objects.requireNonNull(accessor.getFirstNativeHeader("X-AUTH-TOKEN"))
            ));
        }
        
        return message;
    }
}