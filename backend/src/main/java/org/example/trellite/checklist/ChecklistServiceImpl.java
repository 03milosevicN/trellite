package org.example.trellite.checklist;

import lombok.RequiredArgsConstructor;
import org.example.trellite.card.CardRepository;
import org.example.trellite.checklist.dto.ChecklistRequest;
import org.example.trellite.checklist.dto.ChecklistResponse;
import org.example.trellite.common.ResourceNotFoundException;
import org.example.trellite.item.ItemServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ChecklistServiceImpl {

    private final ChecklistRepository checklistRepository;
    private final CardRepository cardRepository;
    private final ChecklistMapper checklistMapper;
    private final ItemServiceImpl itemService;


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
                .filter(q -> q.getId().equals(id))
                .findFirst()
                .orElseThrow( () -> new ResourceNotFoundException("Checklist with id of " + id + " not found."));
        existing.setTitle( dto.getTitle() );
        existing.setIsCompleted( dto.getIsCompleted() );
        return checklistMapper.toResponse(checklistRepository.save(existing));
    }

    public ChecklistResponse patch(String cardId, String id, ChecklistRequest dto) {
        var card = cardRepository
                .findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card with id of " + cardId + "not found"));
        var existing = card
                .getChecklists()
                .stream()
                .filter(q -> q.getId().equals(id))
                .findFirst()
                .orElseThrow( () -> new ResourceNotFoundException("Checklist with id of " + id + " not found."));
        if (dto.getTitle() != null) existing.setTitle(dto.getTitle());
        if (dto.getIsCompleted() != null ) existing.setIsCompleted(dto.getIsCompleted());
        return checklistMapper.toResponse(checklistRepository.save(existing));
    }

    public void delete(String cardId, String id) {
        deleteByCardId(cardId);
        itemService.deleteByChecklistId(id);
        checklistRepository.delete(checklistRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Checklist with id of " + id + " not found.")));
    }


    public void deleteByCardId(String cardId) {
        List<Checklist> checklists = checklistRepository.findByCardId(cardId).orElseThrow(() -> new ResourceNotFoundException("Checklist with id of " + cardId + "not found"));
        checklists.forEach(checklist ->
                itemService.deleteByChecklistId(checklist.getId()));
    }

}
