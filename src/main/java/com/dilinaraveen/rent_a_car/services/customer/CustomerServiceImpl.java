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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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

}
