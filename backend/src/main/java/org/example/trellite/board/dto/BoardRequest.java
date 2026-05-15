package org.example.trellite.board.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.Instant;

@Data
public class BoardRequest {
    @NotBlank(message = "Organization ID cannot be blank.")
    private Long orgId;
    @NotBlank(message = "Title should not be blank.")
    private String title;
    @FutureOrPresent(message = "Creation date cannot be in the past.")
    private Instant createdAt;
    private Boolean archived;
}
