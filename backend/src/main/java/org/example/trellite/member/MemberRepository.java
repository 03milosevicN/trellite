package org.example.trellite.member;

import lombok.NonNull;
import org.example.trellite.org.Organization;
import org.example.trellite.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @NonNull Optional<Member> findById(Long id);

    @Query("SELECT m FROM Member m WHERE m.organization = :org AND m.user = :user")
    Optional<Member> findByOrganizationAndUser(Organization org, User user);

    @Modifying
    @Transactional
    void deleteByOrganizationIdAndUserId(Long orgId, Long userId);

    boolean existsByOrganizationIdAndUserId(Long orgId, Long userId);

}
