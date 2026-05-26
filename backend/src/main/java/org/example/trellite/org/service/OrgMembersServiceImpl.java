package org.example.trellite.org.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trellite.board.BoardServiceImpl;
import org.example.trellite.board.dto.BoardResponse;
import org.example.trellite.boardList.dto.BoardListResponse;
import org.example.trellite.card.dto.CardResponse;
import org.example.trellite.common.BaseService;
import org.example.trellite.common.ResourceNotFoundException;
import org.example.trellite.org.dto.*;
import org.example.trellite.org.mapper.OrgMembersMapper;
import org.example.trellite.org.mapper.OrganizationMapper;
import org.example.trellite.org.repository.OrgMembersRepository;
import org.example.trellite.org.repository.OrganizationRepository;
import org.example.trellite.user.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrgMembersServiceImpl implements BaseService<OrgMembersRequest, OrgMembersResponse, Long> {

    private final OrgMembersRepository orgMembersRepository;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;

    private final OrganizationMapper organizationMapper;
    private final OrgMembersMapper orgMembersMapper;

    private final BoardServiceImpl boardService;

    @Override
    public List<OrgMembersResponse> getAll() {
        return orgMembersRepository
                .findAll()
                .stream()
                .map(orgMembersMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrgMembersResponse getById(Long id) {
        return orgMembersRepository
                .findByOrgMembersId(id)
                .map(orgMembersMapper::toResponse)
                .orElseThrow( () -> new ResourceNotFoundException("Organization member entry with id of " + id + " not found."));
    }

    @Override
    public OrgMembersResponse save(OrgMembersRequest dto) {
        var model = orgMembersMapper.toModel(dto);
        var saved = orgMembersRepository.save(model);
        return orgMembersMapper.toResponse(saved);
    }

    @Override
    public OrgMembersResponse update(Long id, OrgMembersRequest dto) {
        var existing = orgMembersRepository
                .findByOrgMembersId(id)
                .orElseThrow( () -> new ResourceNotFoundException("Organization member entry with id of " + id + " not found."));
        var user = userRepository
                .findByUserId(dto.getUserId())
                .orElseThrow( () -> new ResourceNotFoundException("User with id of " + dto.getUserId() + " not found."));
        var org = organizationRepository
                .findByOrgId(dto.getOrgId())
                .orElseThrow( () -> new ResourceNotFoundException("Organization with id of " + dto.getOrgId() + " not found."));
        existing.setUser(user);
        existing.setOrganization(org);
        existing.setRole(dto.getRole());
        return orgMembersMapper.toResponse(orgMembersRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        orgMembersRepository.delete(
                orgMembersRepository
                        .findByOrgMembersId(id)
                        .orElseThrow( () -> new ResourceNotFoundException("Organization member entry with id of " + id + " not found.") )
        );
    }

    public OrgMembersResponse updateUser(Long orgMembersId, UserTransferRequest req) {
        var existing = orgMembersRepository
                .findByOrgMembersId(orgMembersId)
                .orElseThrow( () -> new ResourceNotFoundException("Organization member entry with id of " + orgMembersId + " not found."));
        var user = userRepository
                .findByUserId(req.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id of " + req.getUserId() + " not found."));
        if (req.getUserId() != null) existing.setUser(user);
        return orgMembersMapper.toResponse(orgMembersRepository.save(existing));
    }

    public OrgMembersResponse updateOrg(Long orgMembersId, OrganizationTransferRequest req) {
        var existing = orgMembersRepository
                .findByOrgMembersId(orgMembersId)
                .orElseThrow( () -> new ResourceNotFoundException("Organization member entry with id of " + orgMembersId + " not found."));
        var org = organizationRepository
                .findByOrgId(req.getOrgId())
                .orElseThrow( () -> new ResourceNotFoundException("Organization with id of " + req.getOrgId() + " not found."));
        if (req.getOrgId() != null) existing.setOrganization(org);
        return orgMembersMapper.toResponse(orgMembersRepository.save(existing));
    }

    public OrgMembersResponse updateRole(Long orgMembersId, RoleUpdateRequest req) {
        var existing = orgMembersRepository
                .findByOrgMembersId(orgMembersId)
                .orElseThrow( () -> new ResourceNotFoundException("Organization member entry with id of " + orgMembersId + " not found."));
        if (req.getRole() != null) existing.setRole(req.getRole());
        return orgMembersMapper.toResponse(orgMembersRepository.save(existing));
    }

    /**
     * Given an org. member's (user) id, fetch all associated cards.
     * @param userId org. member's (user) id.
     * @return list of associated {@link CardResponse} objects.
     */
    public List<CardResponse> getAllCardsByUserIdViaBoards(Long userId) {
        var org = orgMembersRepository
                .findOrgByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization member based on their user id " + userId + " not found."));
        var boards = boardService
                .getByOrgId(org.getOrgId())
                .stream();

        return boards.flatMap(board -> boardService.getCardsByBoardId(board.getId()).stream()).toList();
    }

    public List<BoardListResponse> getAllBoardListsByUserId(Long userId) {
        var org = orgMembersRepository
                .findOrgByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization member based on their user id " + userId + " not found."));
        var boards = boardService.getByOrgId(org.getOrgId());
        var res = boards.stream().flatMap(board -> boardService.getBoardListsByBoardId(board.getId()).stream()).toList();
        log.info("Fetched {} board lists from {} boards", res.size(), boards.size());
        return res;
    }

    public List<BoardResponse> getAllBoardsByUserId(Long userId) {
        var org = orgMembersRepository
                .findOrgByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization member based on their user id " + userId + " not found."));
        return boardService.getByOrgId(org.getOrgId()).stream().toList();
    }

    public List<OrganizationResponse> findOrgsByUserId(Long userId) {
        var res = orgMembersRepository
                .findOrgByUserId(userId)
                .stream()
                .map(organizationMapper::toResponse)
                .toList();
        if (res.isEmpty()) {
            log.warn("No organizations associated with userId={}", userId);
        } else {
            for (var r : res) {
                log.info("Associated orgs for userId={} : {}", r, userId);
            }
        }
        return res;
    }

}
