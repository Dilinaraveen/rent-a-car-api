package com.dilinaraveen.rent_a_car.controllers;

import com.dilinaraveen.rent_a_car.dtos.BookACarDto;
import com.dilinaraveen.rent_a_car.dtos.CarDto;
import com.dilinaraveen.rent_a_car.dtos.SearchCarDto;
import com.dilinaraveen.rent_a_car.services.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/cars")
    public ResponseEntity<List<CarDto>> getAllCars(){
        List<CarDto> carDtoList = customerService.getAllCars();
        return ResponseEntity.ok(carDtoList);
    }

    @PostMapping("/cars/book")
    public ResponseEntity<Void> bookACar(@RequestBody BookACarDto bookACarDto) {
        System.out.println("Received DTO: " + bookACarDto);
        boolean success = customerService.bookACar(bookACarDto);
        if (success) return ResponseEntity.status(HttpStatus.CREATED).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/car/{carId}")
    public ResponseEntity<CarDto> getCarById(@PathVariable Long carId) {
        CarDto carDto = customerService.getCarById(carId);
        if (carDto==null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(carDto);
    }

    @GetMapping("/car/bookings/{userId}")
    public ResponseEntity<List<BookACarDto>> getBookingsByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(customerService.getBookingsByUserId(userId));
    }

    @PostMapping("/car/search")
    public ResponseEntity<?> searchCar(@RequestBody SearchCarDto searchCarDto){
        return ResponseEntity.ok(customerService.searchCar(searchCarDto));
    }

    @GetMapping("/car/brands")
    public ResponseEntity<List<String>> getUniqueBrands() {
        List<String> uniqueBrands = customerService.getUniqueBrands();
        return ResponseEntity.ok(uniqueBrands);
    }

    @PutMapping("/car/booking/{bookingId}")
    public ResponseEntity<?> editBooking(
            @PathVariable Long bookingId,
            @RequestBody BookACarDto bookACarDto,
            @RequestHeader("Authorization") String jwt) {
        try {
            boolean success = customerService.editBooking(bookingId, bookACarDto, jwt);
            if (success) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/car/booking/{bookingId}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long bookingId) {
        boolean success = customerService.deleteBooking(bookingId);
        if (success) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
