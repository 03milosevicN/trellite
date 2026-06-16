package org.example.trellite.org.repository;

import org.example.trellite.org.model.OrgMembers;
import org.example.trellite.org.model.Organization;
import org.example.trellite.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Optional;

public interface OrgMembersRepository extends JpaRepository<OrgMembers, Long> {

    Optional<OrgMembers> findByOrgMembersId(Long id);

    @Query("SELECT om.organization FROM OrgMembers om WHERE om.user.userId = :userId")
    Optional<Organization> findOrgByUserId(@Param("userId") Long userId);

    Long user(User user);

    List<OrgMembers> findAllByUser(User user);

}
