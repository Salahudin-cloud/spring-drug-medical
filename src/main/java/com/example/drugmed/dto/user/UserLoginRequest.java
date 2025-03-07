package com.example.drugmed.dto.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginRequest {

    @NotEmpty(message = "Username are required")
    private  String username;

    @NotEmpty(message = "Password are required")
    private String password;

}
