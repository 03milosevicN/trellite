package org.example.trellite.card;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.example.trellite.card.dto.CardRequest;
import org.example.trellite.card.dto.CardResponse;
import org.example.trellite.common.ObjectIdMapper;
import org.example.trellite.common.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final ObjectIdMapper objectIdMapper;


    public List<CardResponse> getAll() {
        return cardRepository
                .findAll()
                .stream()
                .map(cardMapper::toResponse)
                .collect(Collectors.toList());
    }

    public CardResponse getById(String id) {
        return cardRepository
                .findById(id)
                .map(cardMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Card with id of" + id + " not found."));
    }

    @Transactional
    public CardResponse save(CardRequest dto) {
        var model = cardMapper.toModel(dto);

        log.info("Trying to save card with ID of {}", model.getId());

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
        assert auth != null;
        log.info("{} saved new card:  {}", auth.getName(), dto.getTitle());
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
