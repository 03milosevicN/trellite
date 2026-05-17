package org.example.trellite.board;

import org.example.trellite.board.dto.BoardRequest;
import org.example.trellite.board.dto.BoardResponse;
import org.example.trellite.common.ObjectIdMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ObjectIdMapper.class} )
public interface BoardMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "boardLists", ignore = true)
    Board toModel(BoardRequest request);

    @Mapping(
            source = "id",
            target = "id",
            qualifiedByName = "objectIdToString"
    )
    BoardResponse toResponse(Board model);

}
