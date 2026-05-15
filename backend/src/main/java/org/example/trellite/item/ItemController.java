package org.example.trellite.item;

import lombok.RequiredArgsConstructor;
import org.example.trellite.item.dto.ItemRequest;
import org.example.trellite.item.dto.ItemResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemServiceImpl itemService;


    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(itemService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ItemResponse>> getAll() {
        return ResponseEntity.ok(itemService.getAll());
    }

    @PostMapping
    public ResponseEntity<ItemResponse> create(@RequestBody ItemRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.save(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> update(
            @PathVariable String id,
            @RequestBody ItemRequest req
    ) {
        return ResponseEntity.ok(itemService.update(id, req));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemResponse> patch(
            @PathVariable String id,
            @RequestBody ItemRequest req
    ) {
        return ResponseEntity.ok(itemService.patch(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
