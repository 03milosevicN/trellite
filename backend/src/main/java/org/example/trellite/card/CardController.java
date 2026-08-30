package org.example.trellite.card;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.trellite.card.dto.CardRequest;
import org.example.trellite.card.dto.CardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
@Tag(name = "Card")
public class CardController {

    private final CardService cardService;


    @GetMapping("/{id}")
    public ResponseEntity<CardResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(cardService.getById(id));
    }

    @GetMapping("/by-board/{boardId}")
    public ResponseEntity<List<CardResponse>> getByBoardId(@PathVariable String boardId) {
        return ResponseEntity.ok(cardService.getCardsByBoardId(boardId));
    }

    @GetMapping("/by-list/{boardListId}")
    public ResponseEntity<List<CardResponse>> getByBoardListId(@PathVariable String boardListId) {
        return ResponseEntity.ok(cardService.getCardsByBoardListId(boardListId));
    }

    @GetMapping("/my-backlog")
    public ResponseEntity<List<CardResponse>> getMyBacklog() {
        return ResponseEntity.ok(cardService.getMyBacklog());
    }


    @PostMapping
    public ResponseEntity<CardResponse> create(@RequestBody CardRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.onSaveEvent(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardResponse> update(
            @PathVariable String id,
            @RequestBody CardRequest req
    ) {
        return ResponseEntity.ok(cardService.onUpdateEvent(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        cardService.onDeleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @Deprecated
    @PatchMapping("/{id}")
    public ResponseEntity<CardResponse> patch(
            @PathVariable String id,
            @RequestBody CardRequest req
    ) {
        return ResponseEntity.ok(cardService.patch(id, req));
    }

}
