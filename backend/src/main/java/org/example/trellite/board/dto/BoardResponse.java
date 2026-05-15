package org.example.trellite.board.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class BoardResponse {
    private String id;
    private Long orgId;
    private String title;
    private Instant createdAt;
    private Boolean archived;
}
