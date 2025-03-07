package com.example.drugmed.dto.user;

import com.example.drugmed.entity.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterRequest {

    @NotEmpty(message = "Name are required")
    private String name;

    @NotEmpty(message = "Username are required")
    private String username;

    @NotEmpty(message = "Password are required")
    private String password;

    @NotNull(message = "Role are required")
    private User.RoleBase role ;

    @NotNull(message = "Unit type are required")
    private User.UnitType unitType;
}
