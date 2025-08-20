package com.example.fooddeliveryproject.service;

import com.example.fooddeliveryproject.Configuration.JwtService;
import com.example.fooddeliveryproject.ExceptionHandling.ConstraintValidationException;
import com.example.fooddeliveryproject.User.UserAccount;
import com.example.fooddeliveryproject.User.UserAccountRepository;
import com.example.fooddeliveryproject.User.UserAccountRequestBean;
import com.example.fooddeliveryproject.User.UserAccountResponseBean;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserAccountRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    // Register new user (no auto-login)
    public String register(UserAccountRequestBean request) {
        if (userRepo.findByUsername(request.getUsername()).isPresent()) {
            throw new ConstraintValidationException("Error","The username '" + request.getUsername() + "' is already taken.");
        }

        UserAccount user = UserAccount.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole()) // directly from request
                .build();

        userRepo.save(user);
        return "User '" + request.getUsername() + "' registered successfully with role: " + request.getRole();
    }


    // Login existing user -> generate both tokens
    public UserAccountResponseBean login(UserAccountRequestBean req) {
        try {
            var auth = new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword());
            authManager.authenticate(auth);

            UserDetails ud = userDetailsService.loadUserByUsername(req.getUsername());

            String accessToken = jwtService.generateAccessToken(ud);
            //String refreshToken = jwtService.generateRefreshToken(ud);

            return new UserAccountResponseBean("Bearer", accessToken, ud.getUsername());

        } catch (Exception e) {
            throw new ConstraintValidationException("Error","Incorrect username or password. Please try again.");
        }
    }
}
