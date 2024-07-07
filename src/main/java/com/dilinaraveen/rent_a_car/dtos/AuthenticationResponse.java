package com.dilinaraveen.rent_a_car.dtos;

import com.dilinaraveen.rent_a_car.enums.UserRole;
import lombok.Data;

@Data
public class AuthenticationResponse {

    private String jwt;

    private UserRole userRole;

    private Long userId;
}
