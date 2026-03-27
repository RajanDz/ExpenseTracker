package com.expenseTracker.expensetracker.service;


import com.expenseTracker.expensetracker.dto.SignInRequest;
import com.expenseTracker.expensetracker.model.CustomUserDetails;
import com.expenseTracker.expensetracker.model.User;
import com.expenseTracker.expensetracker.repository.UserRepository;
import com.expenseTracker.expensetracker.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;


    public String signin(SignInRequest request){
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        UserDetails user = (UserDetails) authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtUtils.generateToken(user);
    }


}
