package org.example.trellite.board.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.Instant;

@Data
public class BoardResponse {
    @Field("_id")
    private String id;
    @Field("org_id")
    private Long orgId;
    private String title;
    @Field("created_at")
    private Instant createdAt;
    private Boolean archived;
}
