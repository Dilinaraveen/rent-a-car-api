package com.dilinaraveen.rent_a_car.dtos;

import com.dilinaraveen.rent_a_car.enums.UserRole;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String password;
    private UserRole userRole;
}
