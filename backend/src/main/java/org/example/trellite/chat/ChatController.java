package org.example.trellite.chat;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/api/boards/{boardId}/chat")
@RequiredArgsConstructor
@Tag(name = "REST Chat")
public class ChatController {

    private final ChatService chatService;


    @GetMapping
    public ResponseEntity<Page<ChatMessageResponseDto>> getHistory(
            @PathVariable String boardId,
            Pageable pageable,
            Authentication auth
    ) {
        return ResponseEntity.ok(chatService.getHistory(boardId, auth.getName(), pageable));
    }

}
