package org.example.trellite.checklist;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.example.trellite.card.CardRepository;
import org.example.trellite.checklist.dto.ChecklistRequest;
import org.example.trellite.checklist.dto.ChecklistResponse;
import org.example.trellite.common.ObjectIdMapper;
import org.example.trellite.common.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ChecklistService {

    private final CardRepository cardRepository;
    private final ChecklistMapper checklistMapper;
    private final ObjectIdMapper objectIdMapper;


    public List<ChecklistResponse> getAll(String cardId) {
        var card = cardRepository
                .findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card with id of " + cardId + "not found"));
        return card
                .getChecklists()
                .stream()
                .map(checklistMapper::toResponse)
                .collect(Collectors.toList());
    }


    public ChecklistResponse getById(String cardId, String id) {
        var card = cardRepository
                .findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card with id of " + cardId + "not found"));
        return card
                .getChecklists()
                .stream()
                .map(checklistMapper::toResponse)
                .filter(res -> res.getId().equals(id))
                .findFirst()
                .orElseThrow( () -> new ResourceNotFoundException("Checklist with id of " + id + " not found."));
    }

    public ChecklistResponse save(String cardId, ChecklistRequest dto) {
        var card = cardRepository
                .findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card with id of " + cardId + "not found"));
        var model = checklistMapper.toModel(dto);

        model.setId(new ObjectId());

        if (model.getItems() != null) {
            model.getItems().forEach(item -> {
                if (item.getId() == null) item.setId(new ObjectId());
            });
        } else {
            model.setItems(new ArrayList<>());
        }

        card.getChecklists().add(model);
        cardRepository.save(card);
        return checklistMapper.toResponse(model);
    }

    public ChecklistResponse update(String cardId, String id, ChecklistRequest dto) {
        var card = cardRepository
                .findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card with id of " + cardId + "not found"));
        var existing = card
                .getChecklists()
                .stream()
                .filter(q -> q.getId().equals(objectIdMapper.stringToObjectId(id)))
                .findFirst()
                .orElseThrow( () -> new ResourceNotFoundException("Checklist with id of " + id + " not found."));
        existing.setTitle( dto.getTitle() );
        existing.setIsCompleted( dto.getIsCompleted() );
        cardRepository.save(card);
        return checklistMapper.toResponse(existing);
    }

    public ChecklistResponse patch(String cardId, String id, ChecklistRequest dto) {
        var card = cardRepository
                .findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card with id of " + cardId + "not found"));
        var existing = card
                .getChecklists()
                .stream()
                .filter(q -> q.getId().equals(objectIdMapper.stringToObjectId(id)))
                .findFirst()
                .orElseThrow( () -> new ResourceNotFoundException("Checklist with id of " + id + " not found."));
        if (dto.getTitle() != null) existing.setTitle(dto.getTitle());
        if (dto.getIsCompleted() != null ) existing.setIsCompleted(dto.getIsCompleted());
        cardRepository.save(card);
        return checklistMapper.toResponse(existing);
    }

    public void delete(String cardId, String id) {
        var card = cardRepository.findById(cardId).orElseThrow(() -> new ResourceNotFoundException("Card with id of " + cardId + "not found"));
        card.getChecklists().removeIf(c -> c.getId().equals(objectIdMapper.stringToObjectId(id)));
        log.info("Checklist deleted. Current card object with Id of {} : {}", card.getId(), cardRepository.findById(cardId));
        cardRepository.save(card);
    }


}
