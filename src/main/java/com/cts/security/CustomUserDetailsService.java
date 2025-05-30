package com.cts.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cts.entities.User;
import com.cts.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {


	@Autowired
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user= userRepository.findByUsernameOrEmail(username,username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + username));
        System.out.println("HELLOOOO!!!!! WELCOME!!!!!!!");
        System.out.println(user);
        System.out.println(user.getRoles().size());
        List<GrantedAuthority> authorities
                =user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());




        return new org.springframework.security.core.userdetails.
                User(user.getUsername(),
                user.getPassword(), authorities);
    }
}
