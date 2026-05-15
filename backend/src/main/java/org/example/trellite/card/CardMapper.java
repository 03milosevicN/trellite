package org.example.trellite.card;

import org.example.trellite.card.dto.CardRequest;
import org.example.trellite.card.dto.CardResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardMapper {

    Card toModel(CardRequest request);

    CardResponse toResponse(Card model);

}
