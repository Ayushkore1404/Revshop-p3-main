package com.revshop.userservice.dto;

import com.revshop.userservice.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long userId;
    private String name;
    private String email;
    private Role role;
    private String phone;
    private String address;
    private Boolean isActive;
}
