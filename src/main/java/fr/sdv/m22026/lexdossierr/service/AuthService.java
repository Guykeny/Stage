package fr.sdv.m22026.lexdossierr.service;

import fr.sdv.m22026.lexdossierr.dto.LoginRequest;
import fr.sdv.m22026.lexdossierr.dto.LoginResponse;
import fr.sdv.m22026.lexdossierr.entity.User;

import fr.sdv.m22026.lexdossierr.exception.InvalidCredentialsException;
import fr.sdv.m22026.lexdossierr.repository.UserRepository;
import fr.sdv.m22026.lexdossierr.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        User user= userRepository.findByEmail(loginRequest.email()).orElseThrow(()-> new InvalidCredentialsException("Invalid email "));

        if(!passwordEncoder.matches(loginRequest.password(),user.getPassword())) throw new InvalidCredentialsException("Invalid password");
        return new LoginResponse(jwtService.generateToken(user));
    }
}
