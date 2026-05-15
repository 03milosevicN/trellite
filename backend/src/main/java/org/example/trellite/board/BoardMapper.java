package org.example.trellite.board;

import org.example.trellite.board.dto.BoardRequest;
import org.example.trellite.board.dto.BoardResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoardMapper {

    Board toModel(BoardRequest request);

    BoardResponse toResponse(Board model);

}
