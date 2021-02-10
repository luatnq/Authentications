package com.example.userservice.service.impl;

import com.example.demo.LoginRequestDTO;
import com.example.demo.LoginResponseDTO;
import com.example.demo.SignUpRequestDTO;
import com.example.demo.UserDTO;
import com.example.userservice.entity.CustomUserDetails;
import com.example.userservice.entity.ERole;
import com.example.userservice.entity.User;
import com.example.userservice.jwt.JwtTokenProvider;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Slf4j
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected JwtTokenProvider jwtTokenProvider;

    @Autowired
    protected AuthenticationManager authenticationManager;

    @Override
    public UserDTO signup(SignUpRequestDTO signUpRequest) {
        if (!checkExist(signUpRequest.getUsername())) {
            User user = User.builder()
                    .username(signUpRequest.getUsername())
                    .password(encodePassword(signUpRequest))
                    .email(signUpRequest.getEmail())
                    .role(ERole.ROLE_USER)
                    .uid(generateUid())
                    .build();
            log.info("{}", user);
            User createdUser = userRepository.save(user);
            return UserMapper.convertToDTO(createdUser);
        }
        return null;
    }


    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new NullPointerException());
        return getLoginResponse(loginRequest, user);

    }
// sáng em nghịch ngu n lại lỗi cái gì r ấy, k chạy đk :)))

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException(username));
        return CustomUserDetails.create(user);
    }


    @Override
    public UserDetails loadUserByUsername(long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));
        return CustomUserDetails.create(user);
    }

    private boolean checkExist(String username) {
        User user = userRepository.findByUsername(username)
                .orElse(null);
        if (Objects.isNull(user)) return false;
        return true;
    }

    private String encodePassword(SignUpRequestDTO signUpRequest) {
        return passwordEncoder.encode(signUpRequest.getPassword());
    }

    private String generateUid() {
        return RandomStringUtils.random(40, true, true);
    }

    private LoginResponseDTO getLoginResponse(LoginRequestDTO dto, User user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getUsername(),
                            dto.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String accessToken = jwtTokenProvider.generateAccessToken(user);
            String refreshToken = jwtTokenProvider.generateRefreshToken(user);

            return new LoginResponseDTO(accessToken, refreshToken);
        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }
}
