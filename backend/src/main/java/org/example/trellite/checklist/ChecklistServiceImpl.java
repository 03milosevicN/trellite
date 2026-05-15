package org.example.trellite.checklist;

import lombok.RequiredArgsConstructor;
import org.example.trellite.checklist.dto.ChecklistRequest;
import org.example.trellite.checklist.dto.ChecklistResponse;
import org.example.trellite.common.BaseService;
import org.example.trellite.common.ResourceNotFoundException;
import org.example.trellite.item.ItemServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ChecklistServiceImpl implements BaseService<ChecklistRequest, ChecklistResponse, String> {

    private final ChecklistRepository checklistRepository;
    private final ChecklistMapper checklistMapper;
    private final ItemServiceImpl itemService;


    @Override
    public List<ChecklistResponse> getAll() {
        return checklistRepository
                .findAll()
                .stream()
                .map(checklistMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ChecklistResponse getById(String id) {
        return checklistRepository
                .findById(id)
                .map(checklistMapper::toResponse)
                .orElseThrow( () -> new ResourceNotFoundException("Checklist with id of " + id + " not found."));
    }

    @Override
    public ChecklistResponse save(ChecklistRequest dto) {
        var model = checklistMapper.toModel(dto);
        var saved = checklistRepository.save(model);
        return checklistMapper.toResponse(saved);
    }

    @Override
    public ChecklistResponse update(String id, ChecklistRequest dto) {
        var existing = checklistRepository
                .findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Checklist with id of " + id + " not found."));
        existing.setCardId( dto.getCardId() );
        existing.setTitle( dto.getTitle() );
        existing.setIsCompleted( dto.getIsCompleted() );
        return checklistMapper.toResponse(checklistRepository.save(existing));
    }

    @Override
    public ChecklistResponse patch(String id, ChecklistRequest dto) {
        var existing = checklistRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Checklist with id of " + id + " not found."));
        if (dto.getCardId() != null) existing.setCardId(dto.getCardId());
        if (dto.getTitle() != null) existing.setTitle(dto.getTitle());
        if (dto.getIsCompleted() != null ) existing.setIsCompleted(dto.getIsCompleted());
        return checklistMapper.toResponse(checklistRepository.save(existing));
    }

    @Override
    public void delete(String id) {
        itemService.deleteByChecklistId(id);
        checklistRepository.delete(checklistRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Checklist with id of " + id + " not found.")));
    }

    public void deleteByCardId(String cardId) {
        List<Checklist> checklists = checklistRepository.findByCardId(cardId);
        checklists.forEach(checklist ->
                itemService.deleteByChecklistId(checklist.getId()));
    }

}
