package org.example.trellite.org.controller;

import lombok.RequiredArgsConstructor;
import org.example.trellite.board.dto.BoardResponse;
import org.example.trellite.boardList.dto.BoardListResponse;
import org.example.trellite.card.dto.CardResponse;
import org.example.trellite.common.BaseController;
import org.example.trellite.org.dto.*;
import org.example.trellite.org.service.OrgMembersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/org-members")
@RequiredArgsConstructor
public class OrgMembersController implements BaseController<OrgMembersRequest, OrgMembersResponse, Long> {

    private final OrgMembersServiceImpl orgMembersService;


    @Override
    @GetMapping
    public ResponseEntity<List<OrgMembersResponse>> getAll() {
        return ResponseEntity.ok(orgMembersService.getAll());
    }

    @Override
    @GetMapping("/{orgMembersId}")
    public ResponseEntity<OrgMembersResponse> getById(@PathVariable Long orgMembersId) {
        return ResponseEntity.ok(orgMembersService.getById(orgMembersId));
    }

    @Override
    @PostMapping
    public ResponseEntity<OrgMembersResponse> create(@RequestBody OrgMembersRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orgMembersService.save(req));
    }

    @Override
    @PutMapping("/{orgMembersId}")
    public ResponseEntity<OrgMembersResponse> update(
            @PathVariable Long orgMembersId,
            @RequestBody OrgMembersRequest req
    ) {
        return ResponseEntity.ok(orgMembersService.update(orgMembersId, req));
    }

    @Override
    @DeleteMapping("/{orgMembersId}")
    public ResponseEntity<Void> delete(@PathVariable Long orgMembersId) {
        orgMembersService.delete(orgMembersId);
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/{userId}/cards")
    public ResponseEntity<List<CardResponse>> getAllCardsByUserIdViaBoards(@PathVariable Long userId) {
        return ResponseEntity.ok(orgMembersService.getAllCardsByUserIdViaBoards(userId));
    }


    @GetMapping("/{userId}/board-lists")
    public ResponseEntity<List<BoardListResponse>> getAllBoardListsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(orgMembersService.getAllBoardListsByUserId(userId));
    }


    @GetMapping("/{userId}/boards")
    public ResponseEntity<List<BoardResponse>> getAllBoardsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(orgMembersService.getAllBoardsByUserId(userId));
    }

    @PatchMapping("/{orgMembersId}/user")
    public ResponseEntity<OrgMembersResponse> updateUser(
            @PathVariable Long orgMembersId,
            @RequestBody UserTransferRequest req
    ) {
        return ResponseEntity.ok(orgMembersService.updateUser(orgMembersId, req));
    }

    @PatchMapping("/{orgMembersId}/org")
    public ResponseEntity<OrgMembersResponse> updateOrg(
            @PathVariable Long orgMembersId,
            @RequestBody OrganizationTransferRequest req
            ) {
        return ResponseEntity.ok(orgMembersService.updateOrg(orgMembersId, req));
    }

    @PatchMapping("/{orgMembersId}")
    public ResponseEntity<OrgMembersResponse> updateRole(
            @PathVariable Long orgMembersId,
            @RequestBody RoleUpdateRequest req
    ) {
        return ResponseEntity.ok(orgMembersService.updateRole(orgMembersId, req));
    }

}
