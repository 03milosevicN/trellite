package org.example.trellite.boardList;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trellite.boardList.dto.BoardListRequest;
import org.example.trellite.boardList.dto.BoardListResponse;
import org.example.trellite.card.CardServiceImpl;
import org.example.trellite.card.dto.CardResponse;
import org.example.trellite.common.ObjectIdMapper;
import org.example.trellite.common.ResourceNotFoundException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class BoardListService {

    private final BoardListRepository boardListRepository;
    private final BoardListMapper boardListMapper;
    private final ObjectIdMapper objectIdMapper;
    private final CardServiceImpl cardService;
    private final MongoTemplate mongoTemplate;


    public List<BoardListResponse> getAll() {
        return boardListRepository
                .findAll()
                .stream()
                .peek(q -> log.info("Logging BoardLists: {} : {} : {}", q.getId(),q.getBoardId(), q.getTitle()))
                .map(boardListMapper::toResponse)
                .collect(Collectors.toList());
    }

    public BoardListResponse getById(String id) {
        return boardListRepository.findById(id).map(boardListMapper::toResponse).orElseThrow(() -> new ResourceNotFoundException("BoardList with id of " + id + " not found."));
    }


    public BoardListResponse save(BoardListRequest dto) {
        var model = boardListMapper.toModel(dto);
        var saved = boardListRepository.save(model);
        return boardListMapper.toResponse(saved);
    }

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

    public void delete(String id) {
        cardService.deleteByBoardListId(id);
        boardListRepository.deleteById(id);
    }

    /**
     * Given a board's id, retrieve all boardLists associated with it.
     * @param boardId id of board.
     * @return list of {@link BoardListResponse} objects belonging to board.
     */
    public List<BoardListResponse>findBoardListsByBoardId(String boardId) {
        var mappedBoardId = objectIdMapper.stringToObjectId(boardId);
        return boardListRepository
                .findByBoardId(mappedBoardId)
                .stream()
                .map(boardListMapper::toResponse)
                .toList();
    }

    /**
     * Given a boardList's id, retrieve all cards associated with it.
     * @param boardListId id of board list
     * @return list of {@link CardResponse} objects belonging to boardList.
     */
    public List<CardResponse> getCardsByBoardList(String boardListId) {
        var mappedListId = objectIdMapper.stringToObjectId(boardListId);
        Aggregation aggregation = newAggregation(
                match(Criteria.where("board_list_id").is(mappedListId))
        );
        log.info("INFO: Logging received aggregation: {}", aggregation);
        AggregationResults<CardResponse> results = mongoTemplate.aggregate(aggregation, "cards", CardResponse.class);
        log.info("INFO: Logging aggregation results: {}", results);
        return results.getMappedResults();
    }

}
