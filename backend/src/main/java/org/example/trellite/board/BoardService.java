package org.example.trellite.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trellite.board.dto.BoardRequest;
import org.example.trellite.board.dto.BoardResponse;
import org.example.trellite.boardList.BoardListService;
import org.example.trellite.boardList.dto.BoardListResponse;
import org.example.trellite.card.dto.CardResponse;
import org.example.trellite.common.ObjectIdMapper;
import org.example.trellite.common.ResourceNotFoundException;
import org.example.trellite.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BoardMapper boardMapper;
    private final BoardListService boardListService;
    private final ObjectIdMapper objectIdMapper;


    public List<BoardResponse> getAll() {
        return boardRepository
                .findAll()
                .stream()
                .map(boardMapper::toResponse)
                .collect(Collectors.toList());
    }

    public BoardResponse getById(String id) {
        return boardRepository
                .findById(id)
                .map(boardMapper::toResponse)
                .orElseThrow( () -> new ResourceNotFoundException("Board with id of " + id + " not found."));
    }

    public List<BoardResponse> getByOrgId(Long orgId) {
        var boards = boardRepository.findBoardByOrgId(orgId).orElseThrow(() -> new ResourceNotFoundException("Boards fetched thru org. id of " + orgId + " not found."));
        return boards.stream().map(boardMapper::toResponse).toList();
    }


    public BoardResponse save(BoardRequest dto) {
        var model = boardMapper.toModel(dto);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();

        log.info("{} saved new board:  {}", userEmail, dto.getTitle());

        var user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("User with email of " + userEmail + " not found."));

        if (model.getMembers() == null) {
            model.setMembers(new ArrayList<>());
        }
        model.getMembers().add(user.getId());
        log.info("{} is owner and sole member of board {}", user.getId(), dto.getTitle());
        var saved = boardRepository.save(model);
        log.info("Saved members: {}", saved.getMembers());
        return boardMapper.toResponse(saved);
    }

    public BoardResponse update(String id, BoardRequest dto) {
        var existing = boardRepository
                .findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Board with id of " + id + " not found."));
        existing.setTitle( dto.getTitle() );
        existing.setCreatedAt(Instant.now());
        existing.setArchived(dto.getArchived());
        return boardMapper.toResponse(boardRepository.save(existing));
    }

    public BoardResponse patch(String id, BoardRequest dto) {
        var existing = boardRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Board with id of " + id + " not found."));
        if ( dto.getOrgId() != null ) existing.setOrgId( dto.getOrgId() );
        if ( dto.getTitle() != null ) existing.setTitle( dto.getTitle() );
        if ( dto.getCreatedAt() != null ) existing.setCreatedAt( Instant.now() );
        if ( dto.getArchived() != null ) existing.setArchived( dto.getArchived() );
        return boardMapper.toResponse( boardRepository.save(existing));
    }

    public void delete(String id) {
        var board = boardRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Board with id of " + id + " not found."));

        if (board.getBoardLists() != null) {
            for (var list : board.getBoardLists()) {
                boardListService.delete(objectIdMapper.objectIdToString(list.getId()));
            }
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assert auth != null;
        log.info("{} deleted board:  {}", auth.getName(), board.getTitle());

        boardRepository.deleteById(id);
    }


    /**
     * Returns flat map of cards from boardList streams associated with a board.
     * @param boardId id of board
     * @return list of {@link CardResponse} objects.
     */
    public List<CardResponse> getCardsByBoardId(String boardId) {
        return boardListService.findBoardListsByBoardId(boardId)
                .stream()
                .flatMap(boardList ->
                        boardListService.getCardsByBoardList(boardList.getId()).stream()
                )
                .toList();
    }

    public List<BoardListResponse> getBoardListsByBoardId(String boardId) {
        return boardListService.findBoardListsByBoardId(boardId).stream().toList();
    }

}
