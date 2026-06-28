package org.example.trellite.org;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.trellite.member.Member;

import java.time.Instant;
import java.util.List;

@Entity
@Data
@Table(name = "organizations")
@NoArgsConstructor
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "org_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "created_at")
    private Instant createdAt;

    @OneToMany(
            mappedBy = "organization",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<Member> members;

}
