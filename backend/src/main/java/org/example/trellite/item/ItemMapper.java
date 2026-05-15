package org.example.trellite.item;

import org.example.trellite.item.dto.ItemRequest;
import org.example.trellite.item.dto.ItemResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    Item toModel(ItemRequest request);

    ItemResponse toResponse(Item model);

}
