package org.example.trellite.card.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.Instant;
import java.util.List;

@Data
public class CardRequest {
    @NotBlank(message = "BoardList ID cannot be blank.")
    private String boardListId;
    @NotBlank(message = "Title shouldn't be blank.")
    private String title;
    private String desc;
    private List<Long> assignees;
    private List<String> labels;
    @FutureOrPresent
    private Instant dueDate;
}
