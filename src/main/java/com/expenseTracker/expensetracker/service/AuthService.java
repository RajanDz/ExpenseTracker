package com.expenseTracker.expensetracker.service;


import com.expenseTracker.expensetracker.dto.SignInRequest;
import com.expenseTracker.expensetracker.dto.SignUpRequest;
import com.expenseTracker.expensetracker.exception.InvalidCredentialsException;
import com.expenseTracker.expensetracker.model.User;
import com.expenseTracker.expensetracker.repository.UserRepository;
import com.expenseTracker.expensetracker.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public String signin(SignInRequest request) throws InvalidCredentialsException {
        Authentication authentication;
        try{
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));

        } catch (AuthenticationException ex){
            throw new InvalidCredentialsException("Invalid username or password");
        }



        UserDetails user = (UserDetails) authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtUtils.generateToken(user);
    }

    public User signup(SignUpRequest signUpRequest){
        Optional<User> userByUsername = userRepository.findByUsername(signUpRequest.getUsername());
        if (userByUsername.isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"User with this username already exist");
        }

        Optional<User> userByEmail = userRepository.findByEmail(signUpRequest.getEmail());
        if (userByEmail.isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"User with this email already exist");
        }

        User user = new User(
                signUpRequest.getName(),
                signUpRequest.getLastname(),
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword())
        );
        userRepository.save(user);
        return user;
    }

}
