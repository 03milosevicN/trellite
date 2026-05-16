package org.example.trellite.card.dto;

import lombok.Data;
import org.example.trellite.checklist.Checklist;

import java.time.Instant;
import java.util.List;

@Data
public class CardResponse {
    private String id;
    private String boardListId;
    private String title;
    private String desc;
    private List<Long> assignees;
    private List<String> labels;
    private Instant dueDate;
    private List<Checklist> checklists;
}
