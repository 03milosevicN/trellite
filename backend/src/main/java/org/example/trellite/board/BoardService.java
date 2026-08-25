package org.example.trellite.board;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.example.trellite.board.dto.BoardRequest;
import org.example.trellite.board.dto.BoardResponse;
import org.example.trellite.boardList.BoardListService;
import org.example.trellite.boardList.dto.BoardListResponse;
import org.example.trellite.card.dto.CardResponse;
import org.example.trellite.common.ObjectIdMapper;
import org.example.trellite.common.ResourceNotFoundException;
import org.example.trellite.member.MemberMapper;
import org.example.trellite.member.MemberRepository;
import org.example.trellite.member.dto.MemberResponse;
import org.example.trellite.org.OrganizationRepository;
import org.example.trellite.user.UserRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
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
    private final MongoTemplate mongoTemplate;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final OrganizationRepository organizationRepository;


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

    public List<BoardResponse> getAllByOrgId(Long orgId) {
        var boards = boardRepository.findBoardsByOrgId(orgId).orElseThrow(() -> new ResourceNotFoundException("Boards fetched thru org. id of " + orgId + " not found."));
        return boards.stream().map(boardMapper::toResponse).toList();
    }

    public BoardResponse getOneByOrgId(Long orgId) {
        return boardRepository
                .findBoardByOrgId(orgId)
                .map(boardMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Board fetched thru org. id of " + orgId + " not found."));
    }

    public List<MemberResponse> getBoardMembers(String boardId) {
        Board board = boardRepository.findById(new ObjectId(boardId))
                .orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + boardId));
        if (board.getMembers() == null || board.getMembers().isEmpty()) {
            return Collections.emptyList();
        }

        return memberRepository.findAllByUserIdIn(board.getMembers())
                .stream()
                .map(memberMapper::toResponse)
                .toList();
    }

    public List<MemberResponse> fetchMembers(Long orgId) {
        return memberRepository.findMembersByOrganizationId(orgId).stream().map(memberMapper::toResponse).toList();
    }

    /**
     * @param userId Long value to append to existing board's members list
     * @return Board to which a user got assigned to
     */
    public BoardResponse assignToBoard(Long userId, String boardId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id of " + userId + " not found."));
        var assignedBoard = boardRepository.findById(new ObjectId(boardId)).orElseThrow(() -> new ResourceNotFoundException("Board with id of " + boardId + " not found."));
        var toObjectId = new ObjectId(boardId);

        Query query = Query.query(
                Criteria.where("_id").is(toObjectId)
        );

        Update update = new Update().addToSet("members", userId);

        mongoTemplate.updateFirst(
                query,
                update,
                Board.class
        );

        log.info("Assigning {} (ID: {}) as a member to board {} (ID: {}).", user, userId, assignedBoard, boardId);

        var board = mongoTemplate.findById(toObjectId, Board.class);
        if (board == null) {
            throw new EntityNotFoundException("Board with id of " + boardId + " not found.");
        }

        return boardMapper.toResponse(board);
    }

    /**
     * @param userId User id
     * @return true if user with id of userId is member of particular board
     */
    public boolean isMemberOfBoard(String boardId, Long userId) {
        var result = boardRepository.existsByIdAndMembersContains(new ObjectId(boardId), userId);
        var user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id of " + userId + " not found."));
        var board = boardRepository.findById(new ObjectId(boardId)).orElseThrow(() -> new ResourceNotFoundException("Board with id of " + boardId + " not found."));

        if (!result) {
            log.info("User {} (ID: {}) is not member of board {}.", user, userId, board);
        }
        log.info("User {} (ID: {}) is a member of board {}.", user, userId, board);

        return result;
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

    public void leaveBoard(String boardId, Long userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id of " + userId + " not found."));
        var toObjectId = new ObjectId(boardId);

        var query = new Query(
                Criteria.where("_id").is(toObjectId)
        );

        var update = new Update().pull("members", userId);

        mongoTemplate.updateFirst(
                query,
                update,
                Board.class
        );
        log.info("User {} left board with ID of {}.", user.getEmail(), boardId);
    }

}
