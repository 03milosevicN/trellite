package org.example.trellite.auth;

import lombok.RequiredArgsConstructor;
import org.example.trellite.org.repository.OrgMembersRepository;
import org.example.trellite.user.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final OrgMembersRepository membersRepository;


    @Override
    @NonNull
    @Transactional
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {

        var user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email of " + email + " not found."));

        List<GrantedAuthority> authorities = membersRepository
                .findAllByUser(user)
                .stream()
                // at-runtime formatting, prevents database role bleedover: ORG_{orgs.org_id}_{org_members.role} //
                .map(member -> new SimpleGrantedAuthority(
                        "ORG_" + member.getOrganization().getOrgId() + "_" + member.getRole().name()
                ))
                .collect(Collectors.toList());

        return new AuthenticatedUser(user, authorities);
    }

}
