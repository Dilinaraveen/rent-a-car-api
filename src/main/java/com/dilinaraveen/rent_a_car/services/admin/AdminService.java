package com.dilinaraveen.rent_a_car.services.admin;

import com.dilinaraveen.rent_a_car.dtos.CarDto;

import java.io.IOException;
import java.util.List;

public interface AdminService {
    boolean postCar(CarDto carDto) throws IOException;
    List<CarDto> getAllCars();
    void deleteCar(Long id);
    CarDto getCarById(Long id);

    boolean updateCar(Long CarId, CarDto carDto) throws IOException;
}
