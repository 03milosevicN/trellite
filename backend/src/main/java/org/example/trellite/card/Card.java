package org.example.trellite.card;

import jakarta.persistence.Id;
import lombok.Data;
import org.example.trellite.checklist.Checklist;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.Instant;
import java.util.List;

@Document(collection = "boards")
@Data
public class Card {

    @Id
    @Field(targetType = FieldType.OBJECT_ID)
    private String id;

    @Field(name = "board_list_id")
    private String boardListId;

    @Field(name = "title")
    private String title;

    @Field(name = "desc")
    private String desc;

    @Field(name = "assignees")
    private List<Integer> assignees;

    @Field(name = "labels")
    private List<String> labels;

    @Field(name = "due_date")
    private Instant dueDate;

}
