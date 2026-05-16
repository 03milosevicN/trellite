package org.example.trellite.board;

import org.example.trellite.board.dto.BoardRequest;
import org.example.trellite.board.dto.BoardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardServiceImpl boardService;

    @Autowired
    public BoardController(BoardServiceImpl boardService) {
        this.boardService = boardService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<BoardResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(boardService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<BoardResponse>> getAll() {
        return ResponseEntity.ok(boardService.getAll());
    }

    @PostMapping
    public ResponseEntity<BoardResponse> create(@RequestBody BoardRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.save(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardResponse> update(
            @PathVariable String id,
            @RequestBody BoardRequest req
    ) {
        return ResponseEntity.ok(boardService.update(id, req));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BoardResponse> patch(
            @PathVariable String id,
            @RequestBody BoardRequest req
    ) {
        return ResponseEntity.ok(boardService.patch(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boardService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
