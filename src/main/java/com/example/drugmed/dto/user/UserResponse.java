package com.example.drugmed.dto.user;

import com.example.drugmed.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String username;
    private String password;
    private User.UnitType unit;
    private User.RoleBase role;
    private User.StatusUser status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}