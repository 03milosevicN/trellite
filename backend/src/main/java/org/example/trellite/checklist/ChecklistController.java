package org.example.trellite.checklist;

import lombok.RequiredArgsConstructor;
import org.example.trellite.checklist.dto.ChecklistRequest;
import org.example.trellite.checklist.dto.ChecklistResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/checklists")
@RequiredArgsConstructor
public class ChecklistController {

    private final ChecklistServiceImpl checklistService;


    @GetMapping("/{id}")
    public ResponseEntity<ChecklistResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(checklistService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ChecklistResponse>> getAll() {
        return ResponseEntity.ok(checklistService.getAll());
    }

    @PostMapping
    public ResponseEntity<ChecklistResponse> create(@RequestBody ChecklistRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(checklistService.save(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChecklistResponse> update(
            @PathVariable String id,
            @RequestBody ChecklistRequest req
    ) {
        return ResponseEntity.ok(checklistService.update(id, req));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ChecklistResponse> patch(
            @PathVariable String id,
            @RequestBody ChecklistRequest req
    ) {
        return ResponseEntity.ok(checklistService.patch(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        checklistService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
