package com.dilinaraveen.rent_a_car.repositories;

import com.dilinaraveen.rent_a_car.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
}
