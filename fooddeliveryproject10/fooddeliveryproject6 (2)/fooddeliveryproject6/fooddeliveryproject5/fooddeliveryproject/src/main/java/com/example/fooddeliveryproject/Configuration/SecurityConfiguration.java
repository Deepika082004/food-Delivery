package com.example.fooddeliveryproject.Configuration;

import com.example.fooddeliveryproject.User.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthFilter jwtAuthFilter;
    private final UserAccountRepository userRepo;


    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService uds, PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(uds);
        provider.setPasswordEncoder(encoder);
        return new ProviderManager(provider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // BCrypt hashing
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        //permit All
                        .requestMatchers("/user/register", "/user/login").permitAll()

                        //User
//                        .requestMatchers(HttpMethod.POST, "/customer").hasRole("USER")
//                        .requestMatchers(HttpMethod.GET, "/customer/{id}").hasRole("USER")
//
//                        .requestMatchers(HttpMethod.POST, "/customer/bulk").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.GET,"/customer").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.POST, "/hotel").hasAnyRole("ADMIN","USER")
//                        .requestMatchers(HttpMethod.POST,"/hotel/bulk").hasAnyRole("ADMIN")
//                        .requestMatchers(HttpMethod.POST, "/hotel").hasAnyRole("HOTEL")





                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
