package com.etna.sportevents.security;

import com.etna.sportevents.models.UserEntity;
import com.etna.sportevents.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public JwtUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        if (user != null) {
            return new JwtUserDetails(user);
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    public JwtUserDetails loadUserById(Integer id) throws UsernameNotFoundException {
        UserEntity user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return new JwtUserDetails(user);
        } else {
            throw new UsernameNotFoundException("User not found with id: " + id);
        }
    }

}
