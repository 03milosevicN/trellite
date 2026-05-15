package org.example.trellite.boardList;

import org.example.trellite.boardList.dto.BoardListRequest;
import org.example.trellite.boardList.dto.BoardListResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoardListMapper {

    BoardList toModel(BoardListRequest request);

    BoardListResponse toResponse(BoardList model);

}
