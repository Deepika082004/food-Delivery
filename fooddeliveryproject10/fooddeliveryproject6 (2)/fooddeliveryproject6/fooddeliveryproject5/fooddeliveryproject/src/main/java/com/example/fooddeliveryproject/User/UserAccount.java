package com.example.fooddeliveryproject.User;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
import java.util.regex.Pattern;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String username;
    private String password;
    private String role;

    public Pattern getRoles() {
        return Pattern.compile(role);
    }
}
