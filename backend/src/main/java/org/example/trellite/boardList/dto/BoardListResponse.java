package org.example.trellite.boardList.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Data
public class BoardListResponse {
    @Field("_id")
    private String id;
    @Field("board_id")
    private String boardId;
    private String title;
    @Field("created_at")
    private Instant createdAt;
}
