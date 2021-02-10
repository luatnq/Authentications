package com.example.userservice.service;

import com.example.demo.LoginRequestDTO;
import com.example.demo.LoginResponseDTO;
import com.example.demo.SignUpRequestDTO;
import com.example.demo.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    UserDTO signup(SignUpRequestDTO signUpRequest);

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);

    UserDetails loadUserByUsername(String username);

    UserDetails loadUserByUsername(long id);
}
