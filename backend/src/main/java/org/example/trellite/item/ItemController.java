package org.example.trellite.item;

import lombok.RequiredArgsConstructor;
import org.example.trellite.checklist.ChecklistServiceImpl;
import org.example.trellite.item.dto.ItemRequest;
import org.example.trellite.item.dto.ItemResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cards/{cardId}/checklists/{checklistId}/items")
@RequiredArgsConstructor
public class ItemController {

    private final ChecklistServiceImpl checklistService;
    private final ItemServiceImpl itemService;


    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getById(
            @PathVariable String cardId,
            @PathVariable String checklistId,
            @PathVariable String id
    ) {
        return ResponseEntity.ok(itemService.getById(cardId, checklistId, id));
    }

    @GetMapping
    public ResponseEntity<List<ItemResponse>> getAll(
            @PathVariable String cardId,
            @PathVariable String checklistId
    ) {
        return ResponseEntity.ok(itemService.getAll(cardId, checklistId));
    }

    @PostMapping
    public ResponseEntity<ItemResponse> create(
            @PathVariable String cardId,
            @PathVariable String checklistId,
            @RequestBody ItemRequest req
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.save(cardId, checklistId, req));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemResponse> patch(
            @PathVariable String cardId,
            @PathVariable String checklistId,
            @PathVariable String id,
            @RequestBody ItemRequest req
    ) {
        return ResponseEntity.ok(itemService.patch(cardId, checklistId, id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable String cardId,
            @PathVariable String checklistId,
            @PathVariable String id
    ) {
        itemService.delete(cardId, checklistId, id);
        return ResponseEntity.noContent().build();
    }

}
