package org.example.trellite.board;

import lombok.RequiredArgsConstructor;
import org.example.trellite.board.dto.BoardRequest;
import org.example.trellite.board.dto.BoardResponse;
import org.example.trellite.boardList.BoardListServiceImpl;
import org.example.trellite.common.BaseService;
import org.example.trellite.common.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardServiceImpl implements BaseService<BoardRequest, BoardResponse, String> {

    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;
    private final BoardListServiceImpl boardListService;


    @Override
    public List<BoardResponse> getAll() {
        return boardRepository
                .findAll()
                .stream()
                .map(boardMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BoardResponse getById(String id) {
        return boardRepository
                .findById(id)
                .map(boardMapper::toResponse)
                .orElseThrow( () -> new ResourceNotFoundException("Board with id of " + id + " not found."));
    }

    @Override
    public BoardResponse save(BoardRequest dto) {
        var model = boardMapper.toModel(dto);
        var saved = boardRepository.save(model);
        return boardMapper.toResponse(saved);
    }

    @Override
    public BoardResponse update(String id, BoardRequest dto) {
        var existing = boardRepository
                .findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Board with id of " + id + " not found."));
        existing.setTitle( dto.getTitle() );
        existing.setCreatedAt(Instant.now());
        existing.setArchived(dto.getArchived());
        return boardMapper.toResponse(boardRepository.save(existing));
    }

    @Override
    public BoardResponse patch(String id, BoardRequest dto) {
        var existing = boardRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Board with id of " + id + " not found."));
        if ( dto.getOrgId() != null ) existing.setOrgId( dto.getOrgId() );
        if ( dto.getTitle() != null ) existing.setTitle( dto.getTitle() );
        if ( dto.getCreatedAt() != null ) existing.setCreatedAt( Instant.now() );
        if ( dto.getArchived() != null ) existing.setArchived( dto.getArchived() );
        return boardMapper.toResponse( boardRepository.save(existing));
    }

    @Override
    public void delete(String id) {
        boardListService.deleteByBoardId(id);
        boardRepository.deleteById(id);
    }

}
