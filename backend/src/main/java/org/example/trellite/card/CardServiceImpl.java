package org.example.trellite.card;

import lombok.RequiredArgsConstructor;
import org.example.trellite.card.dto.CardRequest;
import org.example.trellite.card.dto.CardResponse;
import org.example.trellite.checklist.ChecklistServiceImpl;
import org.example.trellite.common.BaseService;
import org.example.trellite.common.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CardServiceImpl implements BaseService<CardRequest, CardResponse, String> {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final ChecklistServiceImpl checklistService;


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
        return cardRepository.findById(id).map(cardMapper::toResponse).orElseThrow(() -> new ResourceNotFoundException("Card with id of" + id + " not found."));
    }

    @Override
    public CardResponse save(CardRequest dto) {
        var model = cardMapper.toModel(dto);
        var saved = cardRepository.save(model);
        return cardMapper.toResponse(saved);
    }

    @Override
    public CardResponse update(String id, CardRequest dto) {
        var existing = cardRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card with id of" + id + " not found."));
        existing.setBoardListId(dto.getBoardListId());
        existing.setTitle(dto.getTitle());
        existing.setDesc(dto.getDesc());
        existing.setAssignees(dto.getAssignees());
        return cardMapper.toResponse(cardRepository.save(existing));
    }

    @Override
    public CardResponse patch(String id, CardRequest dto) {
        var existing = cardRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Card with id of " + id + " not found."));
        if (dto.getBoardListId() != null) existing.setBoardListId(dto.getBoardListId());
        if (dto.getTitle() != null) existing.setTitle(dto.getTitle());
        if (dto.getDesc() != null) existing.setDesc(dto.getDesc());
        if (dto.getAssignees() != null) existing.setAssignees(dto.getAssignees());
        if (dto.getLabels() != null) existing.setLabels(dto.getLabels());
        if (dto.getDueDate() != null) existing.setDueDate(dto.getDueDate());
        return cardMapper.toResponse(cardRepository.save(existing));
    }

    @Override
    public void delete(String id) {
        checklistService.deleteByCardId(id);
        cardRepository.deleteById(id);
    }

    public void deleteByBoardListId(String boardListId) {
        List<Card> cards = cardRepository.findByBoardListId(boardListId);
        cards.forEach(card ->
                checklistService.deleteByCardId(card.getId()));
    }

}
