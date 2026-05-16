package org.example.trellite.card;

import org.example.trellite.card.dto.CardRequest;
import org.example.trellite.card.dto.CardResponse;
import org.example.trellite.checklist.Checklist;
import org.example.trellite.checklist.ChecklistMapper;
import org.example.trellite.checklist.dto.ChecklistResponse;
import org.example.trellite.item.Item;
import org.example.trellite.item.ItemMapper;
import org.example.trellite.item.dto.ItemResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = { ChecklistMapper.class, ItemMapper.class }
)
public interface CardMapper {

    Card toModel(CardRequest request);

    CardResponse toResponse(Card model);

}
