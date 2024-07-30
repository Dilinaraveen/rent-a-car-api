package com.dilinaraveen.rent_a_car.services.auth;

import com.dilinaraveen.rent_a_car.dtos.SignupRequest;
import com.dilinaraveen.rent_a_car.dtos.UserDto;
import com.dilinaraveen.rent_a_car.entities.User;
import com.dilinaraveen.rent_a_car.enums.UserRole;
import com.dilinaraveen.rent_a_car.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;

    @PostConstruct
    public void createAdminAccount() {
        Optional<User> adminAccount = userRepository.findFirstByUserRole(UserRole.ADMIN);

        if (adminAccount.isEmpty()) {
            User newAdminAccount = new User();
            newAdminAccount.setName("Admin");
            newAdminAccount.setEmail("admin@test.com");
            newAdminAccount.setPassword(new BCryptPasswordEncoder().encode("admin"));
            newAdminAccount.setUserRole(UserRole.ADMIN);

            userRepository.save(newAdminAccount);
            System.out.println("Admin account created successfully");
        }
    }

    @Transactional
    @Override
    public UserDto createCustomer(SignupRequest signupRequest) {
        try {
            User user = new User();
            user.setEmail(signupRequest.getEmail());
            user.setName(signupRequest.getName());
            user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
            user.setUserRole(UserRole.CUSTOMER);


            User createdCustomer = userRepository.save(user);
            userRepository.flush();


            UserDto createdUserDto = new UserDto();
            createdUserDto.setId(createdCustomer.getId());
            createdUserDto.setEmail(createdCustomer.getEmail());
            createdUserDto.setUserRole(createdCustomer.getUserRole());

            return createdUserDto;
        } catch (Exception e) {
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
            throw e; // or handle the exception as needed
        }
    }


    @Override
    public boolean hasCustomerWithEmail(String email){
        return userRepository.findFirstByEmail(email).isPresent();
    }
}
