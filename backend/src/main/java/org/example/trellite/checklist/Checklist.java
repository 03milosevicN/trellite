package org.example.trellite.checklist;

import jakarta.persistence.Id;
import lombok.Data;
import org.example.trellite.item.Item;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.List;

@Document(collection = "boards")
@Data
public class Checklist {

    @Id
    @Field(targetType = FieldType.OBJECT_ID)
    private String id;

    @Field(name = "card_id")
    private String cardId;

    @Field(name = "title")
    private String title;

    @Field(name = "is_completed")
    private Boolean isCompleted;

}
