package com.example.fooddeliveryproject.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccountResponseBean {
    private String username;
    private String accessToken;
    //private String refreshToken;

    public UserAccountResponseBean(String bearer, String accessToken, String username) {
        this.username = username;
        this.accessToken = accessToken;
    }
}
