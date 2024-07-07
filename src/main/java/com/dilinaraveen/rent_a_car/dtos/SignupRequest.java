package com.dilinaraveen.rent_a_car.dtos;

import lombok.Data;

@Data
public class SignupRequest {

    private String email;

    private String name;

    private String password;
}
