package org.example.trellite.boardList;

import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Board list")
public class BoardListController {

    private final BoardListService boardListService;


    @GetMapping("/{id}")
    public ResponseEntity<BoardListResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(boardListService.getById(id));
    }

    @GetMapping("/by-board/{boardId}")
    public ResponseEntity<List<BoardListResponse>> getByBoardId(@PathVariable String boardId) {
        return ResponseEntity.ok(boardListService.findBoardListsByBoardId(boardId));
    }

    @PostMapping
    public ResponseEntity<BoardListResponse> create(@RequestBody BoardListRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boardListService.onSaveEvent(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardListResponse> update(
            @PathVariable String id,
            @RequestBody BoardListRequest req
    ) {
        return ResponseEntity.ok(boardListService.onUpdateEvent(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boardListService.onDeleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @Deprecated
    @PatchMapping("/{id}")
    public ResponseEntity<BoardListResponse> patch(
            @PathVariable String id,
            @RequestBody BoardListRequest req
    ) {
        return ResponseEntity.ok(boardListService.patch(id, req));
    }

}