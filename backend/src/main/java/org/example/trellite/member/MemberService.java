package org.example.trellite.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trellite.board.Board;
import org.example.trellite.board.BoardRepository;
import org.example.trellite.board.BoardService;
import org.example.trellite.board.projections.BoardIdProjection;
import org.example.trellite.common.ObjectIdMapper;
import org.example.trellite.common.ResourceNotFoundException;
import org.example.trellite.common.RoleType;
import org.example.trellite.member.dto.MemberResponse;
import org.example.trellite.org.OrganizationMapper;
import org.example.trellite.org.OrganizationRepository;
import org.example.trellite.org.dto.OrganizationResponse;
import org.example.trellite.user.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final OrganizationRepository organizationRepository;
    private final MemberMapper memberMapper;
    private final OrganizationMapper organizationMapper;
    private final BoardRepository boardRepository;
    private final BoardService boardService;
    private final ObjectIdMapper objectIdMapper;


    public MemberResponse getById(Long id) {
        return memberRepository
                .findById(id)
                .map(memberMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Member with id of " + id + " not found."));
    }

    /** Given organization id, fetch all organization members. */
    public List<MemberResponse> getOrgMembers(Long id) {
        return memberRepository.findMembersByOrganizationId(id).stream().map(memberMapper::toResponse).toList();
    }

    /*
    * Given Member m with m.user.id of userId,
    * find list of m's orgs where m.role = RoleType.ADMIN
    */
    public List<OrganizationResponse> getMyAdminships(Long userId) {
        var admin = memberRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Member with id of " + userId + " not found."));
        return memberRepository
                .findAllAdminships(admin.getUser().getId())
                .stream()
                .map(organizationMapper::toResponse)
                .toList();
    }

    /*
     * Given Member m with m.user.id of userId,
     * find list of m's orgs where m.role = RoleType.MEMBER
     */
    public List<OrganizationResponse> getMyMemberships(Long userId) {
        var member = memberRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Member with id of " + userId + " not found."));
        return memberRepository
                .findAllMemberships(member.getUser().getId())
                .stream()
                .map(organizationMapper::toResponse)
                .toList();
    }

    public OrganizationResponse getMyMembership(Long userId, Long orgId) {
        var member = memberRepository
                .findByOrganizationIdAndUserId(orgId, userId).orElseThrow(() -> new ResourceNotFoundException("Membership for user with id " + userId + " in organization with id " + orgId + " not found."));

        return organizationMapper.toResponse(member.getOrganization());
    }

    public List<String> getMyBoardMemberships(Long orgId, Long userId) {
        return boardRepository
                .findBoardIdsByOrgIdAndMember(orgId, userId)
                .stream()
                .map(BoardIdProjection::getId)
                .map(objectIdMapper::objectIdToString)
                .toList();
    }

    public boolean hasRole(String email, Long orgId, RoleType role) {
        return memberRepository.existsByUserEmailAndOrganizationIdAndRole(email, orgId, role);
    }

    @Transactional
    public void leaveOrg(Long orgId, User member) {

        var existing = organizationRepository
                .findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization with id of " + orgId + " not found."));

        memberRepository
                .findByOrganizationAndUser(existing, member)
                .filter(m -> m.getRole().equals(RoleType.MEMBER) || m.getRole().equals(RoleType.ADMIN))
                .orElseThrow(() -> new AccessDeniedException("Unauthorized access. User lacks either of the possible roles."));

        var boards = boardRepository.projectFindByOrgIdAndMembersContaining(orgId, member.getId());
        if (boards == null) {
            throw new NullPointerException("boards field might be null.");
        }
        log.info("Checking memberships to boards for user {} in org {}: {}", member.getEmail(), orgId, boards.size());
        for (var board : boards) {
            boardService.leaveBoard(objectIdMapper.objectIdToString(board.getId()), member.getId());
            log.info("User {} leaving board {} while leaving organization {}", member.getEmail(), board.getId(), orgId);
        }

        memberRepository.deleteByOrganizationIdAndUserId(orgId, member.getId());
        log.info("User {} left organization {} and {} assigned boards", member.getId(), orgId, boards.size());
    }




}