package com.etna.sportevents.services;

import com.etna.sportevents.exceptions.*;
import com.etna.sportevents.models.UserEntity;
import com.etna.sportevents.models.UserRole;
import com.etna.sportevents.payloads.request.LoginPayload;
import com.etna.sportevents.payloads.request.RegisterPayload;
import com.etna.sportevents.payloads.request.UpdateUserPayload;
import com.etna.sportevents.payloads.response.TokenResponse;
import com.etna.sportevents.payloads.response.User;
import com.etna.sportevents.repositories.UserRepository;
import com.etna.sportevents.security.JwtTokenUtil;
import com.etna.sportevents.security.JwtUserDetails;
import com.etna.sportevents.security.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User convertUserEntityToUser(UserEntity userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setUsername(userEntity.getUsername());
        user.setEmail(userEntity.getEmail());
        user.setRole(userEntity.getRole());
        user.setCreationDate(userEntity.getCreationDate());
        user.setUpdatedDate(userEntity.getUpdatedDate());
        return user;
    }

    public User getUserById(Integer id) throws NotFoundException {
        Optional<UserEntity> foundUserEntity = userRepository.findById(id);
        if(foundUserEntity.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        UserEntity userEntity = foundUserEntity.get();
        return convertUserEntityToUser(userEntity);
    }

    public User createUser(RegisterPayload registerPayload) throws AlreadyExistsException {
        if(userRepository.existsByUsername(registerPayload.getUsername())) {
            throw new AlreadyExistsException("Username already exists");
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(registerPayload.getUsername());
        userEntity.setPassword(passwordEncoder.encode(registerPayload.getPassword()));
        userEntity.setEmail(registerPayload.getEmail());
        userEntity.setRole(UserRole.ROLE_USER);
        UserEntity created = userRepository.save(userEntity);
        return convertUserEntityToUser(created);
    }

    public TokenResponse login(LoginPayload loginPayload) throws IncorrectCredentialException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginPayload.getUsername(),
                    loginPayload.getPassword()
            ));
        } catch (Exception e) {
            throw new IncorrectCredentialException("Incorrect username or password");
        }
        final JwtUserDetails jwtUserDetails = jwtUserDetailsService.loadUserByUsername(loginPayload.getUsername());
        final String token = jwtTokenUtil.generateAccessToken(jwtUserDetails);
        final String refreshToken = jwtTokenUtil.generateRefreshToken(token);
        TokenResponse loginResponse = new TokenResponse();
        loginResponse.setAccessToken(token);
        loginResponse.setRefreshToken(refreshToken);
        return loginResponse;
    }

    public TokenResponse refreshToken(
            String oldRefreshToken
    ) throws IncorrectCredentialException {
        if(jwtTokenUtil.isTokenExpired(oldRefreshToken)) {
            throw new IncorrectCredentialException("Invalid refresh token");
        }
        String accessTokenFromRefresh = jwtTokenUtil.getAllClaimsFromToken(oldRefreshToken).get("access_token", String.class);
        int userIdFromRefresh = jwtTokenUtil.getUserIdFromToken(oldRefreshToken);
        int userIdFromAccessToken = jwtTokenUtil.getUserIdFromToken(accessTokenFromRefresh);
        if(userIdFromRefresh != userIdFromAccessToken) {
            throw new IncorrectCredentialException("Invalid refresh token or access token");
        }
        final JwtUserDetails jwtUserDetails = jwtUserDetailsService.loadUserById(userIdFromRefresh);
        final String token = jwtTokenUtil.generateAccessToken(jwtUserDetails);
        final String refreshToken = jwtTokenUtil.generateRefreshToken(token);
        TokenResponse loginResponse = new TokenResponse();
        loginResponse.setAccessToken(token);
        loginResponse.setRefreshToken(refreshToken);
        return loginResponse;
    }

    public User updateUser(
        Integer id,
        JwtUserDetails jwtUserDetails,
        UpdateUserPayload updateUserPayload
    ) throws NotFoundException, ForbiddenException, InvalidArgException {
        Optional<UserEntity> foundUserEntity = userRepository.findById(id);
        if(foundUserEntity.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        // if user is not admin, he can only update his own account
        if(!jwtUserDetails.isAdmin() && !jwtUserDetails.getId().equals(id)) {
            System.out.println(jwtUserDetails.getId() + " trying to update " + id);
            throw new ForbiddenException("You are not allowed to update this user");
        }
        UserEntity userEntity = foundUserEntity.get();
        if(updateUserPayload.getUsername() != null) {
            userEntity.setUsername(updateUserPayload.getUsername());
        }
        if(updateUserPayload.getEmail() != null) {
            userEntity.setEmail(updateUserPayload.getEmail());
        }
        // Only admins can update roles
        if(jwtUserDetails.isAdmin() && updateUserPayload.getRole() != null) {
            userEntity.setRole(updateUserPayload.getRole());
        } else if(updateUserPayload.getRole() != null) {
            throw new ForbiddenException("You are not allowed to update this user");
        }
        if(
            updateUserPayload.getNewPassword() != null
                && updateUserPayload.getNewPasswordConfirm() != null
                && updateUserPayload.getOldPassword() != null
        ) {
            if(!updateUserPayload.getNewPassword().equals(updateUserPayload.getNewPasswordConfirm())) {
                throw new InvalidArgException("New password and new password confirm must be the same");
            }
            if(!passwordEncoder.matches(updateUserPayload.getOldPassword(), userEntity.getPassword())) {
                throw new InvalidArgException("Old password is incorrect");
            }
            userEntity.setPassword(passwordEncoder.encode(updateUserPayload.getNewPassword()));
        }
        UserEntity updated = userRepository.save(userEntity);
        return convertUserEntityToUser(updated);
    }

    public void deleteUser(
        Integer id,
        JwtUserDetails jwtUserDetails
    ) throws NotFoundException, ForbiddenException {
        Optional<UserEntity> foundUserEntity = userRepository.findById(id);
        if(foundUserEntity.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        if(!foundUserEntity.get().getId().equals(jwtUserDetails.getId()) && !jwtUserDetails.isAdmin()) {
            throw new ForbiddenException("You are not allowed to delete this user");
        }
        userRepository.delete(foundUserEntity.get());
    }

    public Page<User> getAllUsers(
        int cursor,
        int size
    ) {
        Pageable pageable = PageRequest.of(
            0,
            size,
            Sort.by("id").ascending()
        );
        Page<UserEntity> userEntities = userRepository.findAllByIdGreaterThan(cursor, pageable);
        return userEntities.map(this::convertUserEntityToUser);
    }
}
