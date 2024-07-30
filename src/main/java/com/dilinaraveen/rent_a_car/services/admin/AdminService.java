package com.dilinaraveen.rent_a_car.services.admin;

import com.dilinaraveen.rent_a_car.dtos.*;
import com.dilinaraveen.rent_a_car.enums.UserRole;

import java.io.IOException;
import java.util.List;

public interface AdminService {
    boolean postCar(CarDto carDto) throws IOException;
    List<CarDto> getAllCars();
    void deleteCar(Long id);
    CarDto getCarById(Long id);
    boolean updateCar(Long CarId, CarDto carDto) throws IOException;
    List<BookACarDto> getBookings();

    boolean changeBookingStatus(Long bookingId, String status);

    CarDtoListDto searchCar(SearchCarDto searchCarDto);

    boolean editBooking(Long bookingId, BookACarDto bookACarDto, String jwt);

    boolean deleteBooking(Long bookingId);

    List<UserDetailsDto> getAllUsers();

    public boolean changeUserRole(Long userId, UserRole role);
}
