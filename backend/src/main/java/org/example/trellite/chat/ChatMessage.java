package org.example.trellite.chat;

import jakarta.persistence.Id;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Data
@Document(collection = "chat_messages")
public class ChatMessage {

    @Id
    private ObjectId id;

    @Field(name = "board_id")
    private String boardId;

    @Field(name = "sender_id")
    private Long senderId;

    @Field(name = "content")
    private String content;

    @Field(name = "sent_at")
    private Instant sentAt;

}
