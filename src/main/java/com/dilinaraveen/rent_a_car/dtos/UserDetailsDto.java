package com.dilinaraveen.rent_a_car.dtos;

import com.dilinaraveen.rent_a_car.enums.UserRole;
import lombok.Data;

@Data
public class UserDetailsDto {
        private Long id;
        private String name;
        private String email;
        private UserRole userRole;

}
