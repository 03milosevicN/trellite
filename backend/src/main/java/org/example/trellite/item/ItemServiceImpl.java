package org.example.trellite.item;

import lombok.RequiredArgsConstructor;
import org.example.trellite.common.BaseService;
import org.example.trellite.item.dto.ItemRequest;
import org.example.trellite.item.dto.ItemResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceImpl implements BaseService<ItemRequest, ItemResponse, String> {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public List<ItemResponse> getAll() {
        return List.of();
    }

    @Override
    public ItemResponse getById(String s) {
        return null;
    }

    @Override
    public ItemResponse save(ItemRequest dto) {
        return null;
    }

    @Override
    public ItemResponse update(String s, ItemRequest dto) {
        return null;
    }

    @Override
    public void delete(String s) {

    }

    public void deleteByChecklistId(String id) {

    }

}
