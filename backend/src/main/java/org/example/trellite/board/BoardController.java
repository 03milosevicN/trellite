package org.example.trellite.board;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.trellite.board.dto.BoardRequest;
import org.example.trellite.board.dto.BoardResponse;
import org.example.trellite.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@Tag(name = "Board")
public class BoardController implements BaseController<BoardRequest, BoardResponse, String> {

    private final BoardServiceImpl boardService;


    @Override
    @GetMapping
    public ResponseEntity<List<BoardResponse>> getAll() {
        return ResponseEntity.ok(boardService.getAll());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<BoardResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(boardService.getById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<BoardResponse> create(@RequestBody BoardRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.save(req));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<BoardResponse> update(
            @PathVariable String id,
            @RequestBody BoardRequest req
    ) {
        return ResponseEntity.ok(boardService.update(id, req));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boardService.delete(id);
        return ResponseEntity.noContent().build();
    }



    @PatchMapping("/{id}")
    public ResponseEntity<BoardResponse> patch(
            @PathVariable String id,
            @RequestBody BoardRequest req
    ) {
        return ResponseEntity.ok(boardService.patch(id, req));
    }

}
