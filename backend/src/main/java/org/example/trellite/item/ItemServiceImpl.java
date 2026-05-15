package org.example.trellite.item;

import lombok.RequiredArgsConstructor;
import org.example.trellite.common.BaseService;
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
public class ItemServiceImpl implements BaseService<ItemRequest, ItemResponse, String> {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;


    @Override
    public List<ItemResponse> getAll() {
        return itemRepository
                .findAll()
                .stream()
                .map(itemMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ItemResponse getById(String id) {
        return itemRepository
                .findById(id)
                .map(itemMapper::toResponse)
                .orElseThrow( () -> new ResourceNotFoundException("Item with id of " + id + " not found.") );
    }

    @Override
    public ItemResponse save(ItemRequest dto) {
        var model = itemMapper.toModel(dto);
        var saved = itemRepository.save(model);
        return itemMapper.toResponse(saved);
    }

    @Override
    public ItemResponse update(String id, ItemRequest dto) {
        var existing = itemRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item with id of " + id + " not found."));
        existing.setChecklistId( dto.getChecklistId() );
        existing.setTask( dto.getTask() );
        return itemMapper.toResponse(itemRepository.save(existing));
    }

    @Override
    public ItemResponse patch(String id, ItemRequest dto) {
        var existing = itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item with id of " + id + " not found."));
        if (dto.getChecklistId() != null) dto.setChecklistId(dto.getChecklistId());
        if (dto.getTask() != null) dto.setTask(dto.getTask());
        return itemMapper.toResponse(itemRepository.save(existing));
    }

    @Override
    public void delete(String id) {
        itemRepository.delete(itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item with id of " + id + " not found.")));
    }

    public void deleteByChecklistId(String checklistId) {
        List<Item> items = itemRepository
                .findByChecklistId(checklistId);
        items.forEach(item -> itemRepository.deleteItemByChecklistId(item.getId()) );
    }

}
