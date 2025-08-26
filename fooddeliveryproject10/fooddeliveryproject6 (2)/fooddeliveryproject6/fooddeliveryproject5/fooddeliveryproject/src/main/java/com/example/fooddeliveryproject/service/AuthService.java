//package com.example.fooddeliveryproject.service;
//
//import com.example.fooddeliveryproject.Configuration.JwtService;
//import com.example.fooddeliveryproject.Entity.FoodCustomer;
//import com.example.fooddeliveryproject.ExceptionHandling.ConstraintValidationException;
//import com.example.fooddeliveryproject.User.UserAccount;
//import com.example.fooddeliveryproject.User.UserAccountRepository;
//import com.example.fooddeliveryproject.User.UserAccountRequestBean;
//import com.example.fooddeliveryproject.User.UserAccountResponseBean;
//import com.example.fooddeliveryproject.repository.CustomerRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationServiceException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class AuthService {
//    private final UserAccountRepository userRepo;
//    private final PasswordEncoder passwordEncoder;
//    private final AuthenticationManager authManager;
//    private final JwtService jwtService;
//    private final UserDetailsService userDetailsService;
//    private final CustomerRepository customerRepo;
//
//    // Register new user (no auto-login)
//    public String register(UserAccountRequestBean request) {
//        // check if username already exists
//        if (userRepo.findByUsername(request.getUsername()).isPresent()) {
//            throw new ConstraintValidationException("Error",
//                    "The username '" + request.getUsername() + "' is already taken.");
//        }
//
//        // build UserAccount
//        UserAccount user = UserAccount.builder()
//                .username(request.getUsername())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .role(request.getRole().toUpperCase()) // ROLE_CUSTOMER / ROLE_ADMIN
//                .build();
//
//        // if role is CUSTOMER, also create a FoodCustomer entry
//        if ("USER".equalsIgnoreCase(user.getRole())) {
//            FoodCustomer customer = FoodCustomer.builder()
//                    .customerName(request.getCustomerName()) // extra field in request
//                    .mobile_no(request.getMobileNo())
//                    .email(request.getEmail())
//                    .address(request.getAddress())
//                    .user(user)
//                    .locationCal(request.getLocationCal())
//                    // link to user
//                    .build();
//
//            user.setCustomer(customer); // set bidirectional relation
//            customerRepo.save(customer); // cascade will save user too
//        } else {
//            // if role is ADMIN, just save user
//            userRepo.save(user);
//        }
//
//        return "User '" + request.getUsername() + "' registered successfully with role: " + user.getRole();
//    }
//
//
//    // Login existing user -> generate both tokens
//    public UserAccountResponseBean login(UserAccountRequestBean req) {
//        try {
//            Authentication authentication = authManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
//            );
//
//            if(authentication.isAuthenticated()) {
//                UserDetails ud = userDetailsService.loadUserByUsername(req.getUsername());
//
//                String accessToken = jwtService.generateAccessToken(ud);
//                //String refreshToken = jwtService.generateRefreshToken(ud);
//
//                return new UserAccountResponseBean("Bearer", accessToken, ud.getUsername());
//
//            }
//            else  {
//                throw new AuthenticationServiceException("Invalid username/password supplied");
//            }
//        } catch (Exception e) {
//            throw new ConstraintValidationException("Error","Incorrect username or password. Please try again.");
//        }
//    }
//
//}
