package com.example.fooddeliveryproject.service;

import com.example.fooddeliveryproject.ExceptionHandling.ConstraintValidationException;
import com.example.fooddeliveryproject.User.UserAccountRepository;
import com.example.fooddeliveryproject.User.UserAccountResponseBean;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private final UserAccountRepository userRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username)
                .map(u -> {
                    User.UserBuilder builder = User.withUsername(u.getUsername())
                            .password(u.getPassword())
                            .roles(u.getRoles().split(",")); // split comma-separated roles
                    return builder.build();
                })
                .orElseThrow(() -> new ConstraintValidationException("error","User not found: " + username));
    }
}
