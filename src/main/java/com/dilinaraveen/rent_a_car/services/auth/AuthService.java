package com.dilinaraveen.rent_a_car.services.auth;

import com.dilinaraveen.rent_a_car.dtos.SignupRequest;
import com.dilinaraveen.rent_a_car.dtos.UserDto;

public interface AuthService {

    UserDto createCustomer(SignupRequest signupRequest);

    boolean hasCustomerWithEmail(String email);
}
