package org.example.trellite.card.dto;

import lombok.Data;
import org.example.trellite.checklist.dto.ChecklistResponse;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.Instant;
import java.util.List;

@Data
public class CardResponse {
    @Field("_id")
    private String id;
    @Field("board_list_id")
    private String boardListId;
    private String title;
    private String desc;
    private List<Long> assignees;
    private List<String> labels;
    @Field("due_date")
    private Instant dueDate;
    private List<ChecklistResponse> checklists;
}
