package org.example.trellite.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trellite.chat.service.ChatService;
import org.example.trellite.chat.dto.ChatMessageRequestDto;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/boards/{boardId}/chat.send")
    public void send(
            @DestinationVariable String boardId,
            ChatMessageRequestDto req,
            Principal principal
    ) {
        var res = chatService.send(boardId, principal.getName(), req.getContent());

        String destination = "/topic/boards" + boardId + "/chat";
        messagingTemplate.convertAndSend(destination, res);

        log.debug("MESSAGE SENT TO DESTINATION: {}", destination);
    }

}
