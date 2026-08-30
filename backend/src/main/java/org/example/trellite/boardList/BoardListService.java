package org.example.trellite.boardList;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trellite.board.BoardRepository;
import org.example.trellite.board.BoardService;
import org.example.trellite.boardList.dto.BoardListRequest;
import org.example.trellite.boardList.dto.BoardListResponse;
import org.example.trellite.card.CardService;
import org.example.trellite.card.dto.CardResponse;
import org.example.trellite.common.Event;
import org.example.trellite.common.EventType;
import org.example.trellite.common.ObjectIdMapper;
import org.example.trellite.common.ResourceNotFoundException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final MongoTemplate mongoTemplate;
    private final CardService cardService;
    private final BoardRepository boardRepository;

    private final SimpMessagingTemplate messagingTemplate;
    private static final String TOPIC_PATH = "/topic/board/";


    public BoardListResponse getById(String id) {
        return boardListRepository.findById(id).map(boardListMapper::toResponse).orElseThrow(() -> new ResourceNotFoundException("BoardList with id of " + id + " not found."));
    }

    // Given particular board and particular board list id (assuming boardList is in board) return board list from that board.
    public BoardListResponse getBoardListByBoardId(String boardId, String boardListId) {
        var board = boardRepository.findById(boardId).orElseThrow();
        return boardListRepository
                .findByBoardId(board.getId())
                .stream()
                .filter(boardList -> boardList.getId().toString().equals(boardListId))
                .findFirst()
                .map(boardListMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("BoardList with id of " + boardListId + " not found."));
    }

    // WebSocket upgrade of save() method.
    public BoardListResponse onSaveEvent(BoardListRequest req) {
        var model = boardListMapper.toModel(req);
        var auth = SecurityContextHolder
                .getContext()
                .getAuthentication();
        if (auth == null) throw new UsernameNotFoundException("Failed to extract email from auth payload.");

        var saved = boardListRepository.save(model);
        var res = boardListMapper.toResponse(saved);

        messagingTemplate.convertAndSend(
            TOPIC_PATH + req.getBoardId(),
            new Event(req.getBoardId(), EventType.LIST_CREATED)
        );
        log.info("Event sent, {} saved new list {}.", auth.getName(), req.getTitle());

        return res;
    }
    public BoardListResponse save(BoardListRequest dto) {
        var model = boardListMapper.toModel(dto);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assert auth != null;
        log.info("{} saved new board-list:  {}", auth.getName(), dto.getTitle());
        var saved = boardListRepository.save(model);
        return boardListMapper.toResponse(saved);
    }

    // WebSocket upgrade of update() method.
    public BoardListResponse onUpdateEvent(String id, BoardListRequest req) {
        var convertedReqId = objectIdMapper.stringToObjectId(req.getBoardId());

        var auth = SecurityContextHolder
                .getContext()
                .getAuthentication();
        if (auth == null) throw new UsernameNotFoundException("Failed to extract email from auth payload.");

        var existing = boardListRepository
                .findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("BoardList with id of " + id + " not found."));
        existing.setBoardId(convertedReqId);
        existing.setTitle(req.getTitle());
        existing.setCreatedAt(Instant.now());

        var saved = boardListRepository.save(existing);
        var res = boardListMapper.toResponse(saved);

        messagingTemplate.convertAndSend(
                TOPIC_PATH + req.getBoardId(),
                new Event(req.getBoardId(), EventType.LIST_UPDATED)
        );
        log.info("Event sent, {} updated list {}.", auth.getName(), req.getTitle());

        return res;
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

    // WebSocket upgrade of delete() method.
    public void onDeleteEvent(String id) {
        var removed = boardListRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BoardList with id of " + id + " not found."));

        var auth = SecurityContextHolder
                .getContext()
                .getAuthentication();
        if (auth == null) throw new UsernameNotFoundException("Failed to extract email from auth payload.");

        cardService.deleteByBoardListId(id);

        messagingTemplate.convertAndSend(
                TOPIC_PATH + removed.getBoardId(),
                new Event(objectIdMapper.objectIdToString(removed.getBoardId()), EventType.LIST_UPDATED)
        );
        log.info("Event sent, {} deleted list {} along with its cards.", auth.getName(), removed.getTitle());

        boardListRepository.deleteById(id);
    }
    public void delete(String id) {
        var removedBoardList = boardListRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("BoardList with id of " + id + " not found."));
        cardService.deleteByBoardListId(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assert auth != null;
        log.info("{} deleted board-list:  {}", auth.getName(), removedBoardList.getTitle());
        boardListRepository.deleteById(id);
    }

    /**
     * Given a board's id, retrieve all boardLists associated with it.
     * @param boardId id of board.
     * @return list of {@link BoardListResponse} objects belonging to board.
     */
    public List<BoardListResponse> findBoardListsByBoardId(String boardId) {
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
