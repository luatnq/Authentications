package com.example.userservice.controller;

import com.example.demo.LoginRequestDTO;
import com.example.demo.LoginResponseDTO;
import com.example.demo.SignUpRequestDTO;
import com.example.demo.UserDTO;
import com.example.userservice.service.UserService;
import com.example.userservice.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/")
@Slf4j
public class AuthenticationController {
    @Autowired
    protected UserService userService;

    @PostMapping("register")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequestDTO signUpRequest) {
        log.info("{}", signUpRequest);
        UserDTO userDTO = userService.signup(signUpRequest);
        log.info(userDTO.toString());

        if (userDTO != null) {
            return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO loginResponseDTO = userService.login(loginRequest);
        if (loginResponseDTO != null) {
            return new ResponseEntity<>(loginResponseDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND);
        }
    }
}
