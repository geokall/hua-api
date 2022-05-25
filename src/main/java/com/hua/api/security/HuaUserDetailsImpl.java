package com.hua.api.security;

import com.hua.api.entity.HuaRole;
import com.hua.api.entity.HuaUser;
import com.hua.api.exception.HuaNotFound;
import com.hua.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class HuaUserDetailsImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public HuaUserDetailsImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public HuaUserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        HuaUser user = userRepository.findByUsername(username);

        if (user == null) {
            throw new HuaNotFound("Could not find user with username = " + username);
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        String role = fetchRole(user);

        SimpleGrantedAuthority simpleGrantedAuthorityRole = new SimpleGrantedAuthority(role);
        grantedAuthorities.add(simpleGrantedAuthorityRole);

        return HuaUserPrincipal.builder()
                .id(user.getId())
                .password(user.getPassword())
                .username(user.getUsername())
                .surname(user.getSurname())
                .name(user.getName())
                .email(user.getEmail())
                .authorities(grantedAuthorities)
                .build();
    }


    private String fetchRole(HuaUser user) {
        Set<HuaRole> roles = user.getRoles();
        //user has only one role in our business
        return roles.stream()
                .map(HuaRole::getName)
                .findFirst()
                .orElse(null);
    }
}
