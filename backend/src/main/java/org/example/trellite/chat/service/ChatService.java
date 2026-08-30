package org.example.trellite.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trellite.chat.ChatMessage;
import org.example.trellite.chat.ChatMessageMapper;
import org.example.trellite.chat.ChatMessageRepository;
import org.example.trellite.chat.dto.ChatMessageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageMapper chatMessageMapper;
    private final ChatAuthorizationService chatAuthorizationService;


    public ChatMessageResponseDto send(
            String boardId,
            String senderEmail,
            String content
    ) {
        if (!chatAuthorizationService.isMember(boardId, senderEmail)) {
            throw new AccessDeniedException("User " + senderEmail + " not a member of board " + boardId + ".");
        }

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Message should not be empty.");
        }

        var message = new ChatMessage();
        message.setBoardId( boardId );
        message.setSenderId( chatAuthorizationService.resolveUserId(senderEmail) );
        message.setContent( content );
        message.setSentAt( Instant.now() );

        var saved = chatMessageRepository.save(message);
        log.info("User {} sent message to board {}.", senderEmail, boardId);

        return chatMessageMapper.toResponse(saved);
    }

    public Page<ChatMessageResponseDto> getHistory(
        String boardId,
        String requesterEmail,
        Pageable pageable
    ) {
        if (!chatAuthorizationService.isMember(boardId, requesterEmail)) {
            throw new AccessDeniedException("User " + requesterEmail + " not member of board " + boardId + ".");
        }

        return chatMessageRepository
                .findByBoardIdOrderBySentAtDesc(boardId, pageable)
                .map(chatMessageMapper::toResponse);
    }

}
