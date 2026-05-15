package org.example.trellite.card;

import lombok.RequiredArgsConstructor;
import org.example.trellite.card.dto.CardRequest;
import org.example.trellite.card.dto.CardResponse;
import org.example.trellite.checklist.ChecklistServiceImpl;
import org.example.trellite.common.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CardServiceImpl implements BaseService<CardRequest, CardResponse, String> {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final ChecklistServiceImpl checklistService;


    @Override
    public List<CardResponse> getAll() {
        return List.of();
    }

    @Override
    public CardResponse getById(String id) {
        return null;
    }

    @Override
    public CardResponse save(CardRequest dto) {
        return null;
    }

    @Override
    public CardResponse update(String id, CardRequest dto) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    public void deleteByBoardListId(String id) {
    }

}
