package com.authserve.authproduct.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.authserve.authproduct.dto.AuthCreateUserRequest;
import com.authserve.authproduct.dto.AuthCreateUserResponse;
import com.authserve.authproduct.dto.AuthLoginRequest;
import com.authserve.authproduct.dto.AuthResponse;
import com.authserve.authproduct.models.RoleEntity;
import com.authserve.authproduct.models.UserEntity;
import com.authserve.authproduct.repository.RoleRepository;
import com.authserve.authproduct.repository.UserRepository;
import com.authserve.authproduct.utils.JwtUtils;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private RoleRepository roleRepository;

        @Autowired
        private UserService userService;

        @Autowired
        private JwtUtils jwtUtils;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                UserEntity userEntity = userRepository.findByUsername(username)
                                .orElseThrow(() -> new UsernameNotFoundException(
                                                "El usuario " + username + " no existe"));
                List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

                userEntity.getRoles().forEach(
                                role -> authorityList.add(
                                                new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));

                return new User(userEntity.getUsername(), userEntity.getPassword(), userEntity.isEnabled(),
                                userEntity.isAccountNonExpired(), userEntity.isCredentialsNonExpired(),
                                userEntity.isAccountNonLocked(),
                                authorityList);
        }

        public AuthCreateUserResponse createUser(AuthCreateUserRequest createUserRequest) throws BadRequestException {
                try {
                        List<String> roleRequest = createUserRequest.roleRequest().roleListName();

                        Set<RoleEntity> roleEntityList = roleRepository.findRoleEntitiesByRoleEnumIn(roleRequest)
                                        .stream().collect(Collectors.toSet());

                        UserEntity userEntity = UserEntity.builder()
                                        .username(createUserRequest.username())
                                        .email(createUserRequest.email())
                                        .password(createUserRequest.password())
                                        .roles(roleEntityList)
                                        .build();
                        userService.create(userEntity);

                        return new AuthCreateUserResponse(userEntity.getUsername(), userEntity.getEmail(),
                                        "Usuario creado correctamente");

                } catch (BadCredentialsException e) {
                        throw new BadCredentialsException("Error creating user: " + e.getMessage());

                }

        }

        public AuthResponse loginUser(AuthLoginRequest authLoginRequest) {

                String username = authLoginRequest.username();
                String password = authLoginRequest.password();

                Authentication authentication = this.authenticate(username, password);

                SecurityContextHolder.getContext().setAuthentication(authentication);

                String accessToken = jwtUtils.createToken(authentication);
                AuthResponse authResponse = new AuthResponse("User loged succcesfully", accessToken, true);
                return authResponse;

        }

        public Authentication authenticate(String username, String password) {
                UserDetails userDetails = this.loadUserByUsername(username);

                if (userDetails == null) {
                        throw new BadCredentialsException(String.format("Invalid username or password"));
                }

                return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
        }

}
