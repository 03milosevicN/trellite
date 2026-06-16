package org.example.trellite.boardList;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.trellite.boardList.dto.BoardListRequest;
import org.example.trellite.boardList.dto.BoardListResponse;
import org.example.trellite.common.BaseController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/board-lists")
@RequiredArgsConstructor
@Tag(name = "Board list")
public class BoardListController implements BaseController<BoardListRequest, BoardListResponse, String> {

    private final BoardListServiceImpl boardListService;


    @Override
    @GetMapping
    public ResponseEntity<List<BoardListResponse>> getAll() {
        return ResponseEntity.ok(boardListService.getAll());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<BoardListResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(boardListService.getById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<BoardListResponse> create(@RequestBody BoardListRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boardListService.save(req));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<BoardListResponse> update(
            @PathVariable String id,
            @RequestBody BoardListRequest req
    ) {
        return ResponseEntity.ok(boardListService.update(id, req));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boardListService.delete(id);
        return ResponseEntity.noContent().build();
    }



    @PatchMapping("/{id}")
    public ResponseEntity<BoardListResponse> patch(
            @PathVariable String id,
            @RequestBody BoardListRequest req
    ) {
        return ResponseEntity.ok(boardListService.patch(id, req));
    }

}