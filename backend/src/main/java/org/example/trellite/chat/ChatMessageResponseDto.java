package org.example.trellite.chat;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ChatMessageResponseDto {
    private String id;
    private String boardId;
    private Long senderId;
    /* Denormalized name of the sender. */
    private String senderName;
    private String content;
    private Instant sentAt;
}
