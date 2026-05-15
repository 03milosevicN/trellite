package org.example.trellite.boardList;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.trellite.boardList.dto.BoardListRequest;
import org.example.trellite.boardList.dto.BoardListResponse;
import org.example.trellite.card.CardServiceImpl;
import org.example.trellite.common.BaseService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardListServiceImpl implements BaseService<BoardListRequest, BoardListResponse, String> {

    private final BoardListRepository boardListRepository;
    private final BoardListMapper boardListMapper;
    private final CardServiceImpl cardService;


    @Override
    public List<BoardListResponse> getAll() {
        return List.of();
    }

    @Override
    public BoardListResponse getById(String id) {
        return null;
    }

    @Override
    public BoardListResponse save(BoardListRequest dto) {
        return null;
    }

    @Override
    public BoardListResponse update(String id, BoardListRequest dto) {
        return null;
    }

    @Override
    public void delete(String id) {
        cardService.deleteByBoardListId(id);
        boardListRepository.deleteById(id);
    }

    public void deleteByBoardId(String boardId) {
        List<BoardList> lists = boardListRepository
                .findByBoardId(boardId);
        lists.forEach( list ->
                cardService.deleteByBoardListId(list.getId()) );
    }

}
