package org.example.trellite.card;

import lombok.Data;
import org.example.trellite.checklist.Checklist;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;

@Document(collection = "boards")
@Data
public class Card {

    @Field
    @Indexed(unique = true)
    private String id;

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

    @Field(name = "checklists")
    private List<Checklist> checklists;

}
