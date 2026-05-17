package org.example.trellite.card;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.example.trellite.card.dto.CardRequest;
import org.example.trellite.card.dto.CardResponse;
import org.example.trellite.checklist.ChecklistServiceImpl;
import org.example.trellite.common.BaseService;
import org.example.trellite.common.ObjectIdMapper;
import org.example.trellite.common.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CardServiceImpl implements BaseService<CardRequest, CardResponse, String> {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final ObjectIdMapper objectIdMapper;


    @Override
    public List<CardResponse> getAll() {
        return cardRepository
                .findAll()
                .stream()
                .map(cardMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CardResponse getById(String id) {
        return cardRepository
                .findById(id)
                .map(cardMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Card with id of" + id + " not found."));
    }

    @Override
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

        var saved = cardRepository.save(model);
        return cardMapper.toResponse(saved);
    }

    @Override
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

    @Override
    public void delete(String id) {
        cardRepository.deleteById(id);
    }

    public void deleteByBoardListId(String boardListId) {
        cardRepository.deleteAllByBoardListId(objectIdMapper.stringToObjectId(boardListId));
    }

}
