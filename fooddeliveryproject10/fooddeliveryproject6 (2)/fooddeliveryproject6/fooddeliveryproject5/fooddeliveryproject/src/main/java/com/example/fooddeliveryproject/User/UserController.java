package com.example.fooddeliveryproject.User;

import com.example.fooddeliveryproject.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final AuthService userService;

    @PostMapping("/register")
    public String createUser(@RequestBody UserAccountRequestBean userDto) {
        return userService.register(userDto);
    }

    @PostMapping("/login")
    public UserAccountResponseBean Login(@RequestBody UserAccountRequestBean userDto) {
        return userService.login(userDto);
    }
}
