package org.example.trellite.item;

import lombok.RequiredArgsConstructor;
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
@Transactional
@RequiredArgsConstructor
public class ItemServiceImpl {

    private final CardRepository cardRepository;
    private final ChecklistRepository checklistRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;



    public void deleteByChecklistId(String checklistId) {
        List<Item> items = itemRepository
                .findByChecklistId(checklistId);
        items.forEach(item -> itemRepository.deleteItemByChecklistId(item.getId()) );
    }

}
