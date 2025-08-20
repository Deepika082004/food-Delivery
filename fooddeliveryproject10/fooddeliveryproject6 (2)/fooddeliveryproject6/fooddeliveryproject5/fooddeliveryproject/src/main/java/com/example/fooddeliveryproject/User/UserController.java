package com.example.fooddeliveryproject.User;

import com.example.fooddeliveryproject.RequestBean.UserAccountRequestBean;
import com.example.fooddeliveryproject.ResponseBean.UserAccountResponseBean;
import com.example.fooddeliveryproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserAccountResponseBean createUser(@RequestBody UserAccountRequestBean userDto) {
        return userService.createUser(userDto);
    }

    @GetMapping("/{id}")
    public UserAccountResponseBean getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<UserAccountResponseBean> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{id}")
    public UserAccountResponseBean updateUser(@PathVariable UUID id, @RequestBody UserAccountRequestBean userDto) {
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return "User with id " + id + " deleted successfully.";
    }
}
