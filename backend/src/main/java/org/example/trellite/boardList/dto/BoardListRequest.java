package org.example.trellite.boardList.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.Instant;

@Data
public class BoardListRequest {
    @NotBlank(message = "Board ID cannot be blank.")
    private String boardId;
    @NotBlank(message = "Title should not be blank")
    private String title;
    @FutureOrPresent
    private Instant createdAt;
}
