package org.example.trellite.boardList;

import org.example.trellite.boardList.dto.BoardListRequest;
import org.example.trellite.boardList.dto.BoardListResponse;
import org.example.trellite.common.ObjectIdMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ObjectIdMapper.class} )
public interface BoardListMapper {

    @Mapping(
            target = "id",
            ignore = true
    )
    @Mapping(target = "boardId", ignore = true)
    BoardList toModel(BoardListRequest request);

    @Mapping(
            source = "id",
            target = "id",
            qualifiedByName = "objectIdToString"
    )
    @Mapping(
            source = "boardId",
            target = "boardId",
            qualifiedByName = "objectIdToString"
    )
    BoardListResponse toResponse(BoardList model);

}
