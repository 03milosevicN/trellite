package org.example.trellite.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.example.trellite.card.CardRepository;
import org.example.trellite.common.ObjectIdMapper;
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
    private final ItemMapper itemMapper;
    private final ObjectIdMapper objectIdMapper;


    public List<ItemResponse> getAll(String cardId, String checklistId) {
        var card = cardRepository
                .findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card with id of " + cardId + "not found"));
        var checklist = card
                .getChecklists()
                .stream()
                .filter(c -> c.getId().equals(objectIdMapper.stringToObjectId(checklistId)))
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
                .filter(c -> c.getId().equals(objectIdMapper.stringToObjectId(checklistId)))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Checklist with id of " + checklistId + "not found"));
        return checklist
                .getItems()
                .stream()
                .filter(i -> i.getId().equals(objectIdMapper.stringToObjectId(id)))
                .map(itemMapper::toResponse)
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
                .filter(c -> c.getId().equals(objectIdMapper.stringToObjectId(checklistId)))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Checklist with id of " + checklistId + "not found"));
        var model = itemMapper.toModel(req);
        model.setId(new ObjectId());
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
                .filter(c -> c.getId().equals(objectIdMapper.stringToObjectId(checklistId)))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Checklist with id of " + checklistId + "not found"));
        var item = checklist
                .getItems()
                .stream()
                .filter(i -> i.getId().equals(objectIdMapper.stringToObjectId(id)))
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
                .filter(c -> c.getId().equals(objectIdMapper.stringToObjectId(checklistId)))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Checklist with id of " + checklistId + "not found"));
        checklist.getItems().removeIf(i -> i.getId().equals(objectIdMapper.stringToObjectId(id)));
        log.info("Item deleted. Current checklist object with id of {} : {}", checklist.getId(), checklist.getItems());
        cardRepository.save(card);
    }

}
