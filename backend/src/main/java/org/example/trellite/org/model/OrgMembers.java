package org.example.trellite.org.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.trellite.common.Role;
import org.example.trellite.user.User;

@Entity
@Data
@Table(name="org_members")
@NoArgsConstructor
public class OrgMembers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="org_members_id")
    private Long orgMembersId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = false)
    private Organization organization;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

}
