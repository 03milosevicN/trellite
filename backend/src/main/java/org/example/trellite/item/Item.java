package org.example.trellite.item;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "boards")
@Data
public class Item {

    @Field
    @Indexed(unique = true)
    private String id;

    @Field(name = "task")
    private String task;

}
