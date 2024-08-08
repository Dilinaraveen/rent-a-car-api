package com.dilinaraveen.rent_a_car.services.customer;

import com.dilinaraveen.rent_a_car.dtos.BookACarDto;
import com.dilinaraveen.rent_a_car.dtos.CarDto;
import com.dilinaraveen.rent_a_car.dtos.CarDtoListDto;
import com.dilinaraveen.rent_a_car.dtos.SearchCarDto;
import com.dilinaraveen.rent_a_car.entities.BookACar;
import com.dilinaraveen.rent_a_car.entities.Car;
import com.dilinaraveen.rent_a_car.entities.User;
import com.dilinaraveen.rent_a_car.enums.BookCarStatus;
import com.dilinaraveen.rent_a_car.repositories.BookACarRepository;
import com.dilinaraveen.rent_a_car.repositories.CarRepository;
import com.dilinaraveen.rent_a_car.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{

    private final CarRepository carRepository;

    private final UserRepository userRepository;

    private final BookACarRepository bookACarRepository;

    @Override
    public List<CarDto> getAllCars() {
        return carRepository.findAll().stream().map(Car::getCarDto).collect(Collectors.toList());
    }

    @Override
    public boolean bookACar(BookACarDto bookACarDto) {
        Optional<Car> optionalCar = carRepository.findById(bookACarDto.getCarId());
        Optional<User> optionalUser = userRepository.findById(bookACarDto.getUserId());
        if (optionalCar.isPresent() && optionalUser.isPresent()){
            Car existingCar = optionalCar.get();
            BookACar bookACar = new BookACar();
            bookACar.setUser(optionalUser.get());
            bookACar.setCar(optionalCar.get());
            bookACar.setBookCarStatus(BookCarStatus.PENDING);

            LocalDate fromDate = bookACarDto.getFromDate();
            LocalDate toDate = bookACarDto.getToDate();

            bookACar.setFromDate(fromDate);
            bookACar.setToDate(toDate);

            long days = ChronoUnit.DAYS.between(fromDate, toDate);
            bookACar.setDays(days);
            bookACar.setPrice(existingCar.getPrice() * days);

            bookACar.setDays(days);
            bookACar.setPrice(existingCar.getPrice() * days);
            bookACar.setPickupLocation(bookACarDto.getPickupLocation());
            bookACar.setPickupTime(bookACarDto.getPickupTime());
            bookACar.setDropTime(bookACarDto.getDropTime());
            bookACar.setContactNumber(bookACarDto.getContactNumber());

            bookACarRepository.save(bookACar);

            return true;
        }
        return false;
    }

    @Override
    public CarDto getCarById(Long carId) {
        Optional<Car> optionalCar = carRepository.findById(carId);
        return optionalCar.map(Car::getCarDto).orElse(null);
    }

    @Override
    public List<BookACarDto> getBookingsByUserId(Long userId) {
        return bookACarRepository.findAllByUserId(userId).stream().map(BookACar::getBookACarDto).collect(Collectors.toList());
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
    public List<String> getUniqueBrands() {
        return carRepository.findAll().stream()
                .map(Car::getBrand)
                .distinct()
                .collect(Collectors.toList());
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

    @Override
    public User updateUserProfile(Long userId, User updatedUser) {
        Optional<User> existingUserOptional = userRepository.findById(userId);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            if (updatedUser.getPassword() != null) {
                existingUser.setPassword(new BCryptPasswordEncoder().encode(updatedUser.getPassword()));
            }
            return userRepository.save(existingUser);
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }

}
