package com.dilinaraveen.rent_a_car.services.admin;

import com.dilinaraveen.rent_a_car.dtos.*;
import com.dilinaraveen.rent_a_car.entities.BookACar;
import com.dilinaraveen.rent_a_car.entities.Car;
import com.dilinaraveen.rent_a_car.entities.User;
import com.dilinaraveen.rent_a_car.enums.BookCarStatus;
import com.dilinaraveen.rent_a_car.repositories.BookACarRepository;
import com.dilinaraveen.rent_a_car.repositories.CarRepository;
import com.dilinaraveen.rent_a_car.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final CarRepository carRepository;

    private final BookACarRepository bookACarRepository;

    private final UserRepository userRepository;

    @Override
    public boolean postCar(CarDto carDto) throws IOException {
        System.out.println("Received CarDto: " + carDto);

        try {
            Car car = new Car();
            car.setName(carDto.getName());
            car.setBrand(carDto.getBrand());
            car.setColor(carDto.getColor());
            car.setPrice(carDto.getPrice());
            car.setYear(carDto.getYear());
            car.setType(carDto.getType());
            car.setDescription(carDto.getDescription());
            car.setTransmission(carDto.getTransmission());
            car.setImage(carDto.getImage().getBytes());
            car.setCarAvg(carDto.getCarAvg());
            car.setSeats(carDto.getSeats());

            carRepository.save(car);

            // Convert image bytes to Base64 string for returnedImage
            carDto.setReturnedImage(Base64.getEncoder().encodeToString(car.getImage()));

            return true;
        } catch (Exception e){
            return false;
        }


    }

    @Override
    public List<CarDto> getAllCars() {
        return carRepository.findAll().stream().map(Car::getCarDto).collect(Collectors.toList());
    }

    @Override
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    @Override
    public CarDto getCarById(Long id) {
        Optional<Car> optionalCar = carRepository.findById(id);
        return optionalCar.map(Car::getCarDto).orElse(null);
    }

    @Override
    public boolean updateCar(Long carId, CarDto carDto) throws IOException {
        Optional<Car> optionalCar = carRepository.findById(carId);
        if (optionalCar.isPresent()){
            Car existingCar = optionalCar.get();

            if (carDto.getImage() != null)
                existingCar.setImage(carDto.getImage().getBytes());
            existingCar.setPrice(carDto.getPrice());
            existingCar.setYear(carDto.getYear());
            existingCar.setType(carDto.getType());
            existingCar.setDescription(carDto.getDescription());
            existingCar.setTransmission(carDto.getTransmission());
            existingCar.setColor(carDto.getColor());
            existingCar.setName(carDto.getName());
            existingCar.setBrand(carDto.getBrand());
            existingCar.setCarAvg(carDto.getCarAvg());
            existingCar.setSeats(carDto.getSeats());
            carRepository.save(existingCar);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public List<BookACarDto> getBookings() {
        return bookACarRepository.findAll().stream().map(BookACar::getBookACarDto).collect(Collectors.toList());
    }

    @Override
    public boolean changeBookingStatus(Long bookingId, String status) {
        Optional<BookACar> optionalBookACar = bookACarRepository.findById(bookingId);
        if (optionalBookACar.isPresent()){
            BookACar existingBookACar = optionalBookACar.get();
            if (Objects.equals(status,"APPROVED"))
                existingBookACar.setBookCarStatus(BookCarStatus.APPROVED);
            else if(Objects.equals(status,"REJECTED"))
                existingBookACar.setBookCarStatus(BookCarStatus.REJECTED);
            else
                existingBookACar.setBookCarStatus(BookCarStatus.PENDING);
            bookACarRepository.save(existingBookACar);
            return true;
        }
        return false;
    }

    @Override
    public CarDtoListDto searchCar(SearchCarDto searchCarDto) {
        Car car = new Car();
        car.setBrand(searchCarDto.getBrand());
        car.setType(searchCarDto.getType());
        car.setTransmission(searchCarDto.getTransmission());
        car.setColor(searchCarDto.getColor());
        ExampleMatcher exampleMatcher =
                ExampleMatcher.matchingAll()
                        .withMatcher("brand", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                        .withMatcher("type", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                        .withMatcher("transmission", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                        .withMatcher("color", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<Car> carExample = Example.of(car, exampleMatcher);
        List<Car> carList = carRepository.findAll(carExample);
        CarDtoListDto carDtoListDto = new CarDtoListDto();
        carDtoListDto.setCarDtoList(carList.stream().map(Car::getCarDto).collect(Collectors.toList()));
        return carDtoListDto;
    }

    @Override
    public boolean editBooking(Long bookingId, BookACarDto bookACarDto, String jwt) {
        Optional<BookACar> optionalBooking = bookACarRepository.findById(bookingId);
        Optional<Car> optionalCar = carRepository.findById(bookACarDto.getCarId());

        if (optionalCar.isPresent() && optionalBooking.isPresent()) {
            Car existingCar = optionalCar.get();
            System.out.println("existingCar"+existingCar);
            BookACar booking = optionalBooking.get();

            booking.setFromDate(bookACarDto.getFromDate());
            booking.setToDate(bookACarDto.getToDate());
            booking.setPickupLocation(bookACarDto.getPickupLocation());
            booking.setPickupTime(bookACarDto.getPickupTime());
            booking.setDropTime(bookACarDto.getDropTime());
            booking.setContactNumber(bookACarDto.getContactNumber());

            LocalDate fromDate = bookACarDto.getFromDate();
            LocalDate toDate = bookACarDto.getToDate();
            long days = ChronoUnit.DAYS.between(fromDate, toDate);
            booking.setDays(days);
            booking.setPrice(existingCar.getPrice() * days);

            bookACarRepository.save(booking);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteBooking(Long bookingId) {
        if (bookACarRepository.existsById(bookingId)) {
            bookACarRepository.deleteById(bookingId);
            return true;
        }
        return false;
    }

    public List<UserDetailsDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserDetailsDto)
                .collect(Collectors.toList());
    }

    private UserDetailsDto mapToUserDetailsDto(User user) {
        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setId(user.getId());
        userDetailsDto.setName(user.getName());
        userDetailsDto.setEmail(user.getEmail());
        userDetailsDto.setUserRole(user.getUserRole());
        return userDetailsDto;
    }
}
