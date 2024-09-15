package com.etna.sportevents.controllers;

import com.etna.sportevents.exceptions.HttpException;
import com.etna.sportevents.exceptions.InvalidArgException;
import com.etna.sportevents.exceptions.NotFoundException;
import com.etna.sportevents.exceptions.ForbiddenException;
import com.etna.sportevents.payloads.request.UpdateUserPayload;
import com.etna.sportevents.payloads.response.CursorMeta;
import com.etna.sportevents.payloads.response.HttpResponse;
import com.etna.sportevents.payloads.response.User;
import com.etna.sportevents.security.JwtUserDetails;
import com.etna.sportevents.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("")
    public ResponseEntity<HttpResponse<List<User>>> getAllUsers(
        @RequestParam(defaultValue = "0") Integer cursor,
          @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<User> users = userService.getAllUsers(
                cursor,
                size
        );
        HttpResponse<List<User>> httpResponse = new HttpResponse<>();
        httpResponse.setMessage("Users retrieved successfully");
        httpResponse.setData(users.getContent());
        httpResponse.setCode("OK");
        httpResponse.setMeta(new CursorMeta(users, cursor, size));
        return ResponseEntity.ok(httpResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<HttpResponse<User>> getUserById(
            @PathVariable("id") @Min(1) int id
    ) throws HttpException {
        try {
            User user = userService.getUserById(id);
            HttpResponse<User> httpResponse = new HttpResponse<>();
            httpResponse.setMessage("User retrieved successfully");
            httpResponse.setData(user);
            httpResponse.setCode("OK");
            return ResponseEntity.ok(httpResponse);
        } catch (NotFoundException e) {
            throw HttpException.notFound(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<HttpResponse<User>> updateUserById(
        @PathVariable("id") @Min(1) int id,
        @Valid @RequestBody UpdateUserPayload updatedUser,
        Authentication authentication
    ) throws HttpException {
        try {
            JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
            User user = userService.updateUser(id, jwtUserDetails, updatedUser);
            HttpResponse<User> httpResponse = new HttpResponse<>();
            httpResponse.setMessage("User updated successfully");
            httpResponse.setData(user);
            httpResponse.setCode("OK");
            return ResponseEntity.ok(httpResponse);
        } catch (ForbiddenException e) {
            throw new HttpException(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (NotFoundException e) {
            throw HttpException.notFound(e.getMessage());
        } catch (InvalidArgException e) {
            throw new HttpException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpResponse<Void>> deleteUserById(
            @PathVariable("id") @Min(1) int id,
            Authentication authentication
    ) throws HttpException {
        try {
            JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
            userService.deleteUser(id, jwtUserDetails);
            HttpResponse<Void> httpResponse = new HttpResponse<>();
            httpResponse.setMessage("User deleted successfully");
            httpResponse.setCode("OK");
            return ResponseEntity.ok(httpResponse);
        } catch (NotFoundException e) {
            throw HttpException.notFound(e.getMessage());
        } catch (ForbiddenException e) {
            throw new HttpException(e.getMessage(), HttpStatus.FORBIDDEN);
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
