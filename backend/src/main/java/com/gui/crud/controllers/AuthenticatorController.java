package com.gui.crud.controllers;


import com.gui.crud.dtos.AuthenticatorDto;
import com.gui.crud.dtos.LoginResponseDto;
import com.gui.crud.dtos.RegisterDto;
import com.gui.crud.infra.security.TokenService;
import com.gui.crud.model.user.User;
import com.gui.crud.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthenticatorController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticatorDto authenticatorDto){
        var usernamePassword = new UsernamePasswordAuthenticationToken(authenticatorDto.email(), authenticatorDto.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User)auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDto registerDto){
        if(this.userRepository.findByLogin(registerDto.email()) != null) return ResponseEntity.ok().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(registerDto.password());

        User newUser = new User(registerDto.email(), encryptedPassword, registerDto.userRole());

        this.userRepository.save(newUser);

        return ResponseEntity.ok().build();
    }
}
