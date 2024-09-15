package com.divyan.ChatApplication.Configuration;


import com.divyan.ChatApplication.Controller.ChatMessage;
import com.divyan.ChatApplication.Controller.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private  final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketDisconnectEvent( SessionDisconnectEvent disconnectEvent ){
        StompHeaderAccessor headerAccessor=StompHeaderAccessor.wrap(disconnectEvent.getMessage());
        String username=(String)headerAccessor.getSessionAttributes().get("username");
        if(username!=null){
            log.info(username+" disconnected");
            var chatMessage= ChatMessage.builder()
                    .type(MessageType.LEAVE)
                    .sender(username)
                    .build();
            messagingTemplate.convertAndSend("/topic/public",chatMessage);
        }
    }
}
