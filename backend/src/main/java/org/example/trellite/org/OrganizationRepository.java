package org.example.trellite.org;

import lombok.NonNull;
import org.example.trellite.member.Member;
import org.example.trellite.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    @NonNull Optional<Organization> findById(@NonNull Long id);

    @Query("SELECT o FROM Organization o WHERE o.ownedBy = :ownedBy")
    Optional<List<Organization>> findOrganizationsByOwnedById(User ownedBy);


}
