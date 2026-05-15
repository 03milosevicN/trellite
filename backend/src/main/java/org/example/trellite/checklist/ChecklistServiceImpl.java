package org.example.trellite.checklist;

import lombok.RequiredArgsConstructor;
import org.example.trellite.checklist.dto.ChecklistRequest;
import org.example.trellite.checklist.dto.ChecklistResponse;
import org.example.trellite.common.BaseService;
import org.example.trellite.item.ItemServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChecklistServiceImpl implements BaseService<ChecklistRequest, ChecklistResponse, String> {

    private final ChecklistRepository checklistRepository;
    private final ChecklistMapper checklistMapper;
    private final ItemServiceImpl itemService;


    @Override
    public List<ChecklistResponse> getAll() {
        return List.of();
    }

    @Override
    public ChecklistResponse getById(String s) {
        return null;
    }

    @Override
    public ChecklistResponse save(ChecklistRequest dto) {
        return null;
    }

    @Override
    public ChecklistResponse update(String s, ChecklistRequest dto) {
        return null;
    }

    @Override
    public void delete(String s) {

    }

    public void deleteByCardId(String cardId) {

    }

}
