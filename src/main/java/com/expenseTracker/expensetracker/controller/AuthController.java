package com.expenseTracker.expensetracker.controller;


import com.expenseTracker.expensetracker.dto.SignInRequest;
import com.expenseTracker.expensetracker.dto.SignInResponse;
import com.expenseTracker.expensetracker.dto.SignUpRequest;
import com.expenseTracker.expensetracker.dto.SignupResponseDto;
import com.expenseTracker.expensetracker.exception.InvalidCredentialsException;
import com.expenseTracker.expensetracker.model.User;
import com.expenseTracker.expensetracker.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> signInRequest(@RequestBody SignInRequest request) throws InvalidCredentialsException {
            String jwt = authService.signin(request);
            return ResponseEntity.ok(new SignInResponse(jwt));
    }


    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@Valid @RequestBody SignUpRequest signUpRequest){
        User user = authService.signup(signUpRequest);
        return ResponseEntity.ok().body(new SignupResponseDto(user.getName(),"Account created"));
    }
}
