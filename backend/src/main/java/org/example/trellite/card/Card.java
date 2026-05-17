package org.example.trellite.card;

import jakarta.persistence.Id;
import lombok.Data;
import org.bson.types.ObjectId;
import org.example.trellite.checklist.Checklist;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.Instant;
import java.util.List;

@Document(collection = "cards")
@Data
public class Card {

    @Id
    private ObjectId id;

    @Field(name = "board_list_id")
    private ObjectId boardListId;

    @Field(name = "title")
    private String title;

    @Field(name = "desc")
    private String desc;

    @Field(name = "assignees")
    private List<Long> assignees;

    @Field(name = "labels")
    private List<String> labels;

    @Field(name = "due_date")
    private Instant dueDate;

    @Field(name = "checklists")
    private List<Checklist> checklists;

}
