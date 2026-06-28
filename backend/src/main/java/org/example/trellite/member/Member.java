package org.example.trellite.member;

import jakarta.persistence.*;
import lombok.Data;
import org.example.trellite.common.RoleType;
import org.example.trellite.org.Organization;
import org.example.trellite.user.User;

@Data
@Entity
@Table(name = "members", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "org_id"}))
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = false)
    private Organization organization;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;

}
