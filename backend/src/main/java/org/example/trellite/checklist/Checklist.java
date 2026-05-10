package org.example.trellite.checklist;

import lombok.Data;
import org.example.trellite.item.Item;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "boards")
@Data
public class Checklist {

    @Field
    @Indexed(unique = true)
    private String id;

    @Field(name = "title")
    private String title;

    @Field(name = "is_completed")
    private Boolean isCompleted;

    @Field(name = "items")
    private List<Item> items;

}
