package com.hua.api.controller;

import com.hua.api.dto.JwtResponseDTO;
import com.hua.api.dto.LoginCredentialsDTO;
import com.hua.api.security.HuaUserDetailsImpl;
import com.hua.api.security.HuaUserPrincipal;
import com.hua.api.security.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("auth")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final HuaUserDetailsImpl huaUserDetails;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtils jwtUtils,
                          HuaUserDetailsImpl huaUserDetails) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.huaUserDetails = huaUserDetails;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginCredentialsDTO dto) {

        var authInputToken = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());

        //redirect to userDetails
        LOGGER.info("Trying to authenticate username:" + dto.getUsername());
        Authentication authentication = authenticationManager.authenticate(authInputToken);

        HuaUserPrincipal userPrincipal = huaUserDetails.loadUserByUsername(authentication.getName());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        JwtResponseDTO jwtResponseDTO = toJwtResponseDTO(authentication, userPrincipal);

        return ResponseEntity.ok().body(jwtResponseDTO);
    }


    private JwtResponseDTO toJwtResponseDTO(Authentication authentication, HuaUserPrincipal userPrincipal) {
        JwtResponseDTO jwtResponseDTO = new JwtResponseDTO();
        jwtResponseDTO.setToken(authentication.getDetails().toString());
        jwtResponseDTO.setUsername(userPrincipal.getUsername());
        jwtResponseDTO.setSurname(userPrincipal.getSurname());
        jwtResponseDTO.setName(userPrincipal.getName());
        jwtResponseDTO.setEmail(userPrincipal.getEmail());

        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        jwtResponseDTO.setRoles(roles);

        return jwtResponseDTO;
    }
}
