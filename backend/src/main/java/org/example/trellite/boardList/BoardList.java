package org.example.trellite.boardList;

import lombok.Data;
import org.example.trellite.card.Card;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.Instant;
import java.util.List;

@Document(collection = "boards")
@Data
public class BoardList {

    @Field
    @Indexed(unique = true)
    private String id;

    @Field(name = "title")
    private String title;

    @Field(name = "created_at")
    private Instant createdAt;

    @Field(name = "cards")
    private List<Card> cards;

}
