package org.example.trellite.boardList;

import jakarta.persistence.Id;
import lombok.Data;
import org.example.trellite.card.Card;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import java.time.Instant;
import java.util.List;

@Document(collection = "board_lists")
@Data
public class BoardList {

    @Id
    @Field(targetType = FieldType.OBJECT_ID)
    private String id;

    @Field(name = "board_id")
    private String boardId;

    @Field(name = "title")
    private String title;

    @Field(name = "created_at")
    private Instant createdAt;


    @DocumentReference(lookup = " 'boardListId' : ?#{#self._id} ")
    private List<Card> cards;

}
