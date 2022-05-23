package com.hua.api.security;

import com.hua.api.entity.HuaUser;
import com.hua.api.exception.HuaNotFound;
import com.hua.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HuaUserDetailsImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public HuaUserDetailsImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HuaUser user = userRepository.findByUsername(username);

        if (user == null) {
            throw new HuaNotFound("Could not find user with username = " + username);
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

//        String userPrincipalRole = fetchRole(user);

        SimpleGrantedAuthority simpleGrantedAuthorityRole = new SimpleGrantedAuthority("test_role");
        grantedAuthorities.add(simpleGrantedAuthorityRole);

        return HuaUserPrincipal.builder()
                .id(user.getId())
                .username(user.getUsername())
                .surname(user.getSurname())
                .name(user.getName())
                .email(user.getEmail())
                .authorities(grantedAuthorities)
                .build();
    }
}
