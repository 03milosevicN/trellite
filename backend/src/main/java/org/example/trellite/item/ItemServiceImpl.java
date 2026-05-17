package org.example.trellite.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trellite.card.CardRepository;
import org.example.trellite.checklist.ChecklistRepository;
import org.example.trellite.common.ResourceNotFoundException;
import org.example.trellite.item.dto.ItemRequest;
import org.example.trellite.item.dto.ItemResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ItemServiceImpl {

    private final CardRepository cardRepository;
    private final ChecklistRepository checklistRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;


    public List<ItemResponse> getAll(String cardId, String checklistId) {
        var card = cardRepository
                .findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card with id of " + cardId + "not found"));
        var checklist = card
                .getChecklists()
                .stream()
                .filter(c -> c.getId().equals(checklistId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Checklist with id of " + checklistId + "not found"));
        return checklist
                .getItems()
                .stream()
                .map(itemMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ItemResponse getById(String cardId, String checklistId, String id) {
        var card = cardRepository
                .findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card with id of " + cardId + "not found"));
        var checklist = card
                .getChecklists()
                .stream()
                .filter(c -> c.getId().equals(checklistId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Checklist with id of " + checklistId + "not found"));
        return checklist
                .getItems()
                .stream()
                .map(itemMapper::toResponse)
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item with id of " + id + "not found"));
    }

    public ItemResponse save(String cardId, String checklistId, ItemRequest req) {
        var card = cardRepository
                .findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card with id of " + cardId + "not found"));
        var checklist = card
                .getChecklists()
                .stream()
                .filter(c -> c.getId().equals(checklistId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Checklist with id of " + checklistId + "not found"));
        var model = itemMapper.toModel(req);
        checklist.getItems().add(model);
        cardRepository.save(card);
        return itemMapper.toResponse(model);
    }

    public ItemResponse patch(String cardId, String checklistId, String id, ItemRequest req) {
        var card = cardRepository
                .findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card with id of " + cardId + "not found"));
        var checklist = card
                .getChecklists()
                .stream()
                .filter(c -> c.getId().equals(checklistId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Checklist with id of " + checklistId + "not found"));
        var item = checklist
                .getItems()
                .stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item with id of " + id + "not found"));
        if ( req.getTask() != null ) item.setTask(req.getTask());
        cardRepository.save(card);
        return itemMapper.toResponse(item);
    }

    public void delete(String cardId, String checklistId, String id) {
        var card = cardRepository
                .findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card with id of " + cardId + "not found"));
        var checklist = card
                .getChecklists()
                .stream()
                .filter(c -> c.getId().equals(checklistId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Checklist with id of " + checklistId + "not found"));
        var removed = checklist
                .getItems()
                .removeIf(i -> i.getId().equals(id));
        log.info("Checking if item was removed based on item id match: {}", removed);
        cardRepository.save(card);
    }


    public void deleteByChecklistId(String checklistId) {
        List<Item> items = itemRepository
                .findByChecklistId(checklistId);
        items.forEach(item -> itemRepository.deleteItemByChecklistId(item.getId()) );
    }

}
