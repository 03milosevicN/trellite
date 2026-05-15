package org.example.trellite.boardList.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class BoardListResponse {
    private String id;
    private String boardId;
    private String title;
    private Instant createdAt;
}
