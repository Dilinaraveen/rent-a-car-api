package com.dilinaraveen.rent_a_car.repositories;

import com.dilinaraveen.rent_a_car.entities.User;
import com.dilinaraveen.rent_a_car.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findFirstByEmail(String email);

    User findByUserRole(UserRole userRole);
}
