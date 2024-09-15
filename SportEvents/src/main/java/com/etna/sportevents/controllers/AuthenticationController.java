package com.etna.sportevents.controllers;

import com.etna.sportevents.exceptions.AlreadyExistsException;
import com.etna.sportevents.exceptions.HttpException;
import com.etna.sportevents.exceptions.IncorrectCredentialException;
import com.etna.sportevents.exceptions.NotFoundException;
import com.etna.sportevents.payloads.request.LoginPayload;
import com.etna.sportevents.payloads.request.RefreshTokenPayload;
import com.etna.sportevents.payloads.request.RegisterPayload;
import com.etna.sportevents.payloads.response.HttpResponse;
import com.etna.sportevents.payloads.response.TokenResponse;
import com.etna.sportevents.payloads.response.User;
import com.etna.sportevents.security.JwtUserDetails;
import com.etna.sportevents.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthenticationController {
    @Autowired
    private UserService authenticationService;

    @GetMapping("/me")
    public ResponseEntity<HttpResponse<User>> getMe(
            Authentication authentication
    ) throws HttpException {
        try {
            JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
            User user = authenticationService.getUserById(jwtUserDetails.getId());
            HttpResponse<User> httpResponse = new HttpResponse<>();
            httpResponse.setMessage("User retrieved successfully");
            httpResponse.setData(user);
            httpResponse.setCode("OK");
            return ResponseEntity.ok(httpResponse);
        } catch (NotFoundException e) {
            throw new HttpException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/register")
    public ResponseEntity<HttpResponse<User>> register(
            @Valid @RequestBody RegisterPayload registerPayload
            ) throws HttpException {
        try {
            User user = authenticationService.createUser(registerPayload);
            HttpResponse<User> httpResponse = new HttpResponse<User>();
            httpResponse.setMessage("User created successfully");
            httpResponse.setData(user);
            httpResponse.setCode("CREATED");
            return ResponseEntity.status(HttpStatus.CREATED).body(httpResponse);
        } catch (AlreadyExistsException e) {
            throw new HttpException(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<HttpResponse<TokenResponse>> login(
            @Valid @RequestBody LoginPayload loginPayload
            ) throws HttpException {
        try {
            TokenResponse tokenResponse = authenticationService.login(loginPayload);
            HttpResponse<TokenResponse> httpResponse = new HttpResponse<TokenResponse>();
            httpResponse.setMessage("User logged in successfully");
            httpResponse.setData(tokenResponse);
            httpResponse.setCode("OK");
            return ResponseEntity.ok(httpResponse);
        } catch (IncorrectCredentialException e) {
            throw new HttpException(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<HttpResponse<TokenResponse>> refresh(
        @Valid @RequestBody RefreshTokenPayload payload
        ) throws HttpException {
        try {
            TokenResponse tokenResponse = authenticationService.refreshToken(payload.getRefreshToken());
            HttpResponse<TokenResponse> httpResponse = new HttpResponse<TokenResponse>();
            httpResponse.setMessage("Token refreshed successfully");
            httpResponse.setData(tokenResponse);
            httpResponse.setCode("OK");
            return ResponseEntity.ok(httpResponse);
        } catch (IncorrectCredentialException e) {
            throw new HttpException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        HashMap<String, String> errors = new HashMap<>();
        e.getConstraintViolations().forEach(constraintViolation -> {
            String propertyPath = constraintViolation.getPropertyPath().toString();
            String message = constraintViolation.getMessage();
            errors.put(propertyPath, message);
        });
        HttpResponse<Void> httpResponse = new HttpResponse<>();
        httpResponse.setMessage("Validation failed");
        httpResponse.setErrors(errors);
        httpResponse.setCode("BAD_REQUEST");
        return new ResponseEntity<>(httpResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpException.class)
    ResponseEntity<?> handleException(HttpException e) {
        HttpResponse<Void> httpResponse = new HttpResponse<>();
        httpResponse.setMessage(e.getMessage());
        httpResponse.setCode("UNEXPECTED_ERROR");
        httpResponse.setErrors(e.getMessage());
        return new ResponseEntity<>(httpResponse, e.getStatus());
    }
}
