package com.hua.api.controller;

import com.hua.api.dto.JwtResponseDTO;
import com.hua.api.dto.LoginCredentialsDTO;
import com.hua.api.exception.HuaForbidden;
import com.hua.api.exception.HuaNotFound;
import com.hua.api.security.HuaUserPrincipal;
import com.hua.api.security.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AuthController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<JwtResponseDTO> authenticateUser(@RequestBody LoginCredentialsDTO dto) {

        try {
            LOGGER.info("Trying to authenticate username: " + dto.getUsername());
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));

            HuaUserPrincipal userDetails = (HuaUserPrincipal) authentication.getPrincipal();

            if (!userDetails.isVerified()) {
                LOGGER.info("User is locked");
                throw new HuaForbidden("Ο λογαριασμός δεν είναι ενεργοποιημένος");
            }

            String jwt = jwtUtils.generateJwtToken(authentication);
            LOGGER.info("Generated jwt successfully");

            SecurityContextHolder.getContext().setAuthentication(authentication);

            JwtResponseDTO jwtResponseDTO = toJwtResponseDTO(jwt, userDetails);

            return ResponseEntity.ok().body(jwtResponseDTO);
        } catch (BadCredentialsException e) {
            LOGGER.info(e.getMessage());
            throw new HuaNotFound("Λάθος κωδικός");
        }
    }


    private JwtResponseDTO toJwtResponseDTO(String jwt, HuaUserPrincipal userPrincipal) {
        JwtResponseDTO jwtResponseDTO = new JwtResponseDTO();
        jwtResponseDTO.setId(userPrincipal.getId());
        jwtResponseDTO.setToken(jwt);
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
