package org.example.trellite.checklist.dto;

import lombok.Data;

@Data
public class ChecklistResponse {
    private String id;
    private String cardId;
    private String title;
    private Boolean isCompleted;
}
