package org.example.trellite.checklist;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.trellite.checklist.dto.ChecklistRequest;
import org.example.trellite.checklist.dto.ChecklistResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cards/{cardId}/checklists")
@RequiredArgsConstructor
@Tag(name = "Checklist")
public class ChecklistController {

    private final ChecklistService checklistService;


    @GetMapping("/{id}")
    public ResponseEntity<ChecklistResponse> getById(
            @PathVariable String cardId,
            @PathVariable String id
    ) {
        return ResponseEntity.ok(checklistService.getById(cardId, id));
    }

    @GetMapping
    public ResponseEntity<List<ChecklistResponse>> getAll(@PathVariable String cardId) {
        return ResponseEntity.ok(checklistService.getAll(cardId));
    }


    @PostMapping
    public ResponseEntity<ChecklistResponse> create(
            @PathVariable String cardId,
            @RequestBody ChecklistRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(checklistService.save(cardId, req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChecklistResponse> update(
            @PathVariable String cardId,
            @PathVariable String id,
            @RequestBody ChecklistRequest req
    ) {
        return ResponseEntity.ok(checklistService.update(cardId, id, req));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ChecklistResponse> patch(
            @PathVariable String cardId,
            @PathVariable String id,
            @RequestBody ChecklistRequest req
    ) {
        return ResponseEntity.ok(checklistService.patch(cardId, id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable String cardId,
            @PathVariable String id
    ) {
        checklistService.delete(cardId, id);
        return ResponseEntity.noContent().build();
    }

}
