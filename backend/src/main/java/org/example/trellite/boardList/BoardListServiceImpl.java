package org.example.trellite.boardList;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trellite.boardList.dto.BoardListRequest;
import org.example.trellite.boardList.dto.BoardListResponse;
import org.example.trellite.card.CardRepository;
import org.example.trellite.card.CardServiceImpl;
import org.example.trellite.common.BaseService;
import org.example.trellite.common.ObjectIdMapper;
import org.example.trellite.common.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class BoardListServiceImpl implements BaseService<BoardListRequest, BoardListResponse, String> {

    private final BoardListRepository boardListRepository;
    private final BoardListMapper boardListMapper;
    private final ObjectIdMapper objectIdMapper;
    private final CardServiceImpl cardService;


    @Override
    public List<BoardListResponse> getAll() {
        return boardListRepository
                .findAll()
                .stream()
                .peek(q -> log.info("Logging BoardLists: {} : {} : {}", q.getId(),q.getBoardId(), q.getTitle()))
                .map(boardListMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BoardListResponse getById(String id) {
        return boardListRepository.findById(id).map(boardListMapper::toResponse).orElseThrow(() -> new ResourceNotFoundException("BoardList with id of " + id + " not found."));
    }

    @Override
    public BoardListResponse save(BoardListRequest dto) {
        var model = boardListMapper.toModel(dto);
        var saved = boardListRepository.save(model);
        return boardListMapper.toResponse(saved);
    }

    @Override
    public BoardListResponse update(String id, BoardListRequest dto) {
        var existing = boardListRepository
                .findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("BoardList with id of " + id + " not found."));
        existing.setBoardId( objectIdMapper.stringToObjectId(dto.getBoardId()) );
        existing.setTitle( dto.getTitle() );
        existing.setCreatedAt(Instant.now());
        return boardListMapper.toResponse(boardListRepository.save(existing));
    }

    public BoardListResponse patch(String id, BoardListRequest dto) {
        var existing = boardListRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("BoardList with id of " + id + " not found."));
        if ( dto.getBoardId() != null ) existing.setBoardId(objectIdMapper.stringToObjectId(dto.getBoardId()));
        if ( dto.getTitle() != null ) existing.setTitle(dto.getTitle());
        if (dto.getCreatedAt() != null) existing.setCreatedAt(Instant.now());
        return boardListMapper.toResponse(boardListRepository.save(existing));
    }

    @Override
    public void delete(String id) {
        cardService.deleteByBoardListId(id);
        boardListRepository.deleteById(id);
    }

}
