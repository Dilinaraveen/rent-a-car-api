package com.dilinaraveen.rent_a_car.controllers;

import com.dilinaraveen.rent_a_car.dtos.*;
import com.dilinaraveen.rent_a_car.services.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;


    @PostMapping("/car")
    public ResponseEntity<?> postCar(@ModelAttribute CarDto carDto) throws IOException {

        System.out.println("CarDto: " + carDto);

        boolean success = adminService.postCar(carDto);

        if (success){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/car")
    public ResponseEntity<?> getAllCars(){
        return ResponseEntity.ok(adminService.getAllCars());
    }

    @DeleteMapping("/car/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id){
        adminService.deleteCar(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/car/{id}")
    public ResponseEntity<CarDto> getCarById(@PathVariable Long id){
       CarDto carDto = adminService.getCarById(id);
       return ResponseEntity.ok(carDto);
    }

    @PutMapping("/car/{carId}")
    public ResponseEntity<Void> updateCar(
            @PathVariable Long carId,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String transmission,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Long price,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) Long carAvg,
            @RequestParam(required = false) String seats,
            @RequestParam(required = false) MultipartFile image
    ) throws IOException {
        CarDto carDto = new CarDto();
        carDto.setId(carId);
        carDto.setBrand(brand);
        carDto.setColor(color);
        carDto.setName(name);
        carDto.setType(type);
        carDto.setTransmission(transmission);
        carDto.setDescription(description);
        carDto.setPrice(price);
        carDto.setYear(year);
        carDto.setCarAvg(carAvg);
        carDto.setSeats(seats);
        carDto.setImage(image);

        try {
            System.out.println("CarDto "+carDto);
            boolean success = adminService.updateCar(carId, carDto);
            if (success)return ResponseEntity.status(HttpStatus.OK).build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/car/bookings")
    public ResponseEntity<List<BookACarDto>> getBookings(){
        return ResponseEntity.ok(adminService.getBookings());
    }

    @PutMapping("/car/booking/{bookingId}/{status}")
    public ResponseEntity<?> changeBookingStatus(@PathVariable Long bookingId, @PathVariable String status){
        boolean success = adminService.changeBookingStatus(bookingId, status);
        if (success) return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/car/search")
    public ResponseEntity<?> searchCar(@RequestBody SearchCarDto searchCarDto){
        return ResponseEntity.ok(adminService.searchCar(searchCarDto));
    }

    @PutMapping("/car/booking/{bookingId}")
    public ResponseEntity<?> editBooking(
            @PathVariable Long bookingId,
            @RequestBody BookACarDto bookACarDto,
            @RequestHeader("Authorization") String jwt) {
        try {
            boolean success = adminService.editBooking(bookingId, bookACarDto, jwt);
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
        boolean success = adminService.deleteBooking(bookingId);
        if (success) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDetailsDto>> getAllUsers() {
        List<UserDetailsDto> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

}
