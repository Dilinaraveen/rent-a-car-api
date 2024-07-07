package com.dilinaraveen.rent_a_car.dtos;

import lombok.Data;

@Data
public class AuthenticationRequest {

    private String email;

    private String password;
}
