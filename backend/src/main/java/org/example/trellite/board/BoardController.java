package org.example.trellite.board;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.example.trellite.board.dto.BoardRequest;
import org.example.trellite.board.dto.BoardResponse;
import org.example.trellite.member.dto.MemberResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@Tag(name = "Board")
public class BoardController {

    private final BoardService boardService;


    @GetMapping
    public ResponseEntity<List<BoardResponse>> getAll() {
        return ResponseEntity.ok(boardService.getAll());
    }

    @GetMapping("{boardId}")
    public ResponseEntity<BoardResponse> getById(@PathVariable String boardId) {
        return ResponseEntity.ok(boardService.getById(boardId));
    }

    @GetMapping("/org/{orgId}")
    public ResponseEntity<List<BoardResponse>> getAllByOrgId(@PathVariable long orgId) {
        return ResponseEntity.ok(boardService.getAllByOrgId(orgId));
    }

    @GetMapping("/{orgId}/members")
    public ResponseEntity<List<MemberResponse>> getMembers(@PathVariable Long orgId) {
        return ResponseEntity.ok (boardService.fetchMembers(orgId));
    }

    @GetMapping("/{boardId}/board-members")
    public ResponseEntity<List<MemberResponse>> getBoardMembers(@PathVariable String boardId) {
        return ResponseEntity.ok(boardService.getBoardMembers(boardId));
    }


    @PostMapping("/{userId}/{boardId}")
    public ResponseEntity<BoardResponse> assignToBoard(
            @PathVariable Long userId,
            @PathVariable String boardId
    ) {
        return ResponseEntity.ok(boardService.assignToBoard(userId, boardId));
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

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> leaveBoard(
            @PathVariable String boardId,
            @RequestParam Long userId
    ) {
        boardService.leaveBoard(boardId, userId);
        return ResponseEntity.noContent().build();
    }

}
