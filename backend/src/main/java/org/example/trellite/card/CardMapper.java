package org.example.trellite.card;

import org.example.trellite.card.dto.CardRequest;
import org.example.trellite.card.dto.CardResponse;
import org.example.trellite.checklist.Checklist;
import org.example.trellite.checklist.ChecklistMapper;
import org.example.trellite.checklist.dto.ChecklistResponse;
import org.example.trellite.common.ObjectIdMapper;
import org.example.trellite.item.Item;
import org.example.trellite.item.ItemMapper;
import org.example.trellite.item.dto.ItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = { ChecklistMapper.class, ItemMapper.class, ObjectIdMapper.class }
)
public interface CardMapper {

    @Mapping(
            source = "boardListId",
            target = "boardListId",
            qualifiedByName = "stringToObjectId"
    )
    @Mapping(
            target = "id",
            ignore = true
    )
    Card toModel(CardRequest request);

    @Mapping(
            source = "id",
            target = "id",
            qualifiedByName = "objectIdToString"
    )
    @Mapping(
            source = "boardListId",
            target = "boardListId",
            qualifiedByName = "objectIdToString"
    )
    CardResponse toResponse(Card model);

}
