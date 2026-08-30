package org.example.trellite.member;

import lombok.NonNull;
import org.example.trellite.common.RoleType;
import org.example.trellite.org.Organization;
import org.example.trellite.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @NonNull Optional<Member> findById(@NonNull Long id);

    List<Member> findByUserId(Long userId);

    @Query("SELECT m FROM Member m WHERE m.organization = :org AND m.user = :user")
    Optional<Member> findByOrganizationAndUser(Organization org, User user);

    List<Member> findMembersByOrganizationId(Long orgId);

    Optional<Member> findByOrganizationIdAndUserId(Long orgId, Long userId);

    @Query("SELECT m.organization FROM Member m WHERE m.user.id = :userId AND m.role = :role")
    List<Organization> findAllOrganizationByUserIdAndRole(Long userId, RoleType role);

    default List<Organization> findAllAdminships(Long userId) {
        return findAllOrganizationByUserIdAndRole(userId, RoleType.ADMIN);
    }

    default List<Organization> findAllMemberships(Long userId) {
        return findAllOrganizationByUserIdAndRole(userId, RoleType.MEMBER);
    }

    List<Member> findAllByUserIdIn(List<Long> userIds);


    @Modifying
    @Transactional
    void deleteByOrganizationIdAndUserId(Long orgId, Long userId);

    // member with particular orgId and userId
    boolean existsByOrganizationIdAndUserId(Long orgId, Long userId);

    // userId-based role check
    @Query("SELECT m FROM Member m WHERE m.user.id = :userId AND m.organization.id = :orgId AND m.role = :role")
    boolean existsByUserIdAndOrganizationIdAndRole(Long userId, Long orgId, RoleType role);

    // email-based role check
    boolean existsByUserEmailAndOrganizationIdAndRole(String email, Long userId, RoleType role);




}
