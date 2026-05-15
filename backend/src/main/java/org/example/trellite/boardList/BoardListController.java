package org.example.trellite.boardList;

import lombok.RequiredArgsConstructor;
import org.example.trellite.boardList.dto.BoardListRequest;
import org.example.trellite.boardList.dto.BoardListResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/board-lists")
@RequiredArgsConstructor
public class BoardListController {

    private final BoardListServiceImpl boardListService;


    @GetMapping("/{id}")
    public ResponseEntity<BoardListResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(boardListService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<BoardListResponse>> getAll() {
        return ResponseEntity.ok(boardListService.getAll());
    }

    @PostMapping
    public ResponseEntity<BoardListResponse> create(@RequestBody BoardListRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boardListService.save(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardListResponse> update(
            @PathVariable String id,
            @RequestBody BoardListRequest req
    ) {
        return ResponseEntity.ok(boardListService.update(id, req));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BoardListResponse> patch(
            @PathVariable String id,
            @RequestBody BoardListRequest req
    ) {
        return ResponseEntity.ok(boardListService.patch(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boardListService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
