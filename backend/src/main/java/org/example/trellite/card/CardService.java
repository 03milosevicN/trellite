package org.example.trellite.card;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.example.trellite.board.BoardMapper;
import org.example.trellite.board.BoardRepository;
import org.example.trellite.boardList.BoardListMapper;
import org.example.trellite.boardList.BoardListRepository;
import org.example.trellite.boardList.BoardListService;
import org.example.trellite.card.dto.CardRequest;
import org.example.trellite.card.dto.CardResponse;
import org.example.trellite.common.ObjectIdMapper;
import org.example.trellite.common.ResourceNotFoundException;
import org.example.trellite.common.UnauthorizedException;
import org.example.trellite.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final ObjectIdMapper objectIdMapper;
    private final BoardListRepository boardListRepository;
    private final UserRepository userRepository;


    public CardResponse getById(String id) {
        return cardRepository
                .findById(id)
                .map(cardMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Card with id of" + id + " not found."));
    }

    public List<CardResponse> getCardsByBoardListId(String boardListId) {
        if (!boardListRepository.existsById(boardListId)) {
            throw new ResourceNotFoundException("BoardList with id of " + boardListId + " does not exist.");
        }
        return cardRepository
                .findByBoardListId( objectIdMapper.stringToObjectId(boardListId))
                .stream()
                .map(cardMapper::toResponse)
                .toList();
    }

    public List<CardResponse> getMyBacklog() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedException("User is not authenticated");
        }

        var userEmail = auth.getName();
        var user = userRepository
                .findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + userEmail + " not found."));

        log.info("Fetching backlog for {} (Id: {})", userEmail, user.getId());
        return cardRepository
                .findByAssigneesContainingAndBoardListIdIsNull(user.getId())
                .stream()
                .map(cardMapper::toResponse)
                .toList();
    }

    @Transactional
    public CardResponse save(CardRequest dto) {
        var model = cardMapper.toModel(dto);

        if (model.getChecklists() != null) {
            model.getChecklists().forEach(checklist -> {
                if (checklist.getId() == null) checklist.setId(new ObjectId());
                if (checklist.getItems() != null ) {
                    checklist.getItems().forEach(item -> {
                        if (item.getId() == null) item.setId(new ObjectId());
                    });
                }
            });
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String userEmail = auth.getName();
            var user = userRepository
                    .findByEmail(userEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("User with email " + userEmail + " not found."));

            if (model.getAssignees() == null) {
                model.setAssignees(new ArrayList<>());
            }

            if (!model.getAssignees().contains(user.getId())) {
                model.getAssignees().add(user.getId());
            }
            log.info("{} saved new card:  {}", auth.getName(), dto.getTitle());
        }

        var saved = cardRepository.save(model);
        return cardMapper.toResponse(saved);
    }

    @Transactional
    public CardResponse update(String id, CardRequest dto) {
        var existing = cardRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card with id of" + id + " not found."));
        existing.setBoardListId(objectIdMapper.stringToObjectId(dto.getBoardListId()));
        existing.setTitle(dto.getTitle());
        existing.setDesc(dto.getDesc());
        existing.setAssignees(dto.getAssignees());
        existing.setLabels(dto.getLabels());
        existing.setDueDate(dto.getDueDate());
        return cardMapper.toResponse(cardRepository.save(existing));
    }

    @Transactional
    public CardResponse patch(String id, CardRequest dto) {
        var existing = cardRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card with id of " + id + " not found."));
        if (dto.getBoardListId() != null) existing.setBoardListId(objectIdMapper.stringToObjectId(dto.getBoardListId()));
        if (dto.getTitle() != null) existing.setTitle(dto.getTitle());
        if (dto.getDesc() != null) existing.setDesc(dto.getDesc());
        if (dto.getAssignees() != null) existing.setAssignees(dto.getAssignees());
        if (dto.getLabels() != null) existing.setLabels(dto.getLabels());
        if (dto.getDueDate() != null) existing.setDueDate(dto.getDueDate());
        return cardMapper.toResponse(cardRepository.save(existing));
    }

    public void delete(String id) {
        var removedCard = cardRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Card with id of " + id + " not found."));
        var auth = SecurityContextHolder.getContext().getAuthentication();
        assert auth != null;
        log.info("{} deleted card: {}", auth.getName(), removedCard.getTitle());
        cardRepository.deleteById(id);
    }

    @Transactional
    public void deleteByBoardListId(String boardListId) {
        cardRepository.deleteAllByBoardListId(objectIdMapper.stringToObjectId(boardListId));
    }

}
