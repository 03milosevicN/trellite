package org.example.trellite.card;

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
public class CardController {

    private final CardServiceImpl cardService;


    @GetMapping("/{id}")
    public ResponseEntity<CardResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(cardService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<CardResponse>> getAll() {
        return ResponseEntity.ok(cardService.getAll());
    }

    @PostMapping
    public ResponseEntity<CardResponse> create(@RequestBody CardRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.save(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardResponse> update(
            @PathVariable String id,
            @RequestBody CardRequest req
    ) {
        return ResponseEntity.ok(cardService.update(id, req));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CardResponse> patch(
            @PathVariable String id,
            @RequestBody CardRequest req
    ) {
        return ResponseEntity.ok(cardService.patch(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        cardService.delete(id);
        return ResponseEntity.noContent().build();
    }



}
