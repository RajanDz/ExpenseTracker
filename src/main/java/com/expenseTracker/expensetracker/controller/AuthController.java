package com.expenseTracker.expensetracker.controller;


import com.expenseTracker.expensetracker.dto.SignInRequest;
import com.expenseTracker.expensetracker.dto.SignInResponse;
import com.expenseTracker.expensetracker.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> signInRequest(@RequestBody SignInRequest request){
            String jwt = authService.signin(request);
            return ResponseEntity.ok(new SignInResponse(jwt));
    }
}
